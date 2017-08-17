package Control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Model.Documento;
import Model.Termo;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Busca{

    private Map<String, List<Documento>> listaInvertida;
    private List<Documento> docs;
    
    
    public void run() {
        docs = new ArrayList<Documento>();
        List<String> termosConsulta = new ArrayList<String>();
        termosConsulta.add("previdencia");
        termosConsulta.add("reforma");

        Boolean validaDominio = false;

        Robo robo = new Robo();
        String[] urls = new String[2];
        urls[0] = "http://economia.estadao.com.br/noticias/geral,relator-da-reforma-da-previdencia-na-camara-admite-eventual-mudanca-no-plenario,70001849417";
        urls[1] = "http://www.redebrasilatual.com.br/politica/2017/06/reforma-da-previdencia-esta-derrotada-dizem-parlamentares";
        List<Parser> parsers;

        // for(int i = 0; i < parsers.size();i++){
        do {
            parsers = robo.baixarPaginaSimultaneo(urls, 2);
            for (int i = 0; i < parsers.size(); i++) {
                Parser parser = parsers.get(i);
                Documento doc = new Documento();
                doc.setCentroide(parser.getCentroide());
                doc.setLink(parser.getLink());
                doc.setQtdTermos(parser.getQtdTermos());
                doc.setQtdTermosDiferentes(parser.getQtdTermosDiferentes());
                doc.setSeeds(parser.getLinksUrl(parser.getLink()));
                doc.setTitle(parser.getTitle());

                robo.addLinks(doc.getSeeds());
                robo.visitedLink(doc.getLink());

                validaDominio = true;
                if(doc.getTitle().contains(termosConsulta.get(0))){
                    for (String termo : termosConsulta) {
                        
                        if (!doc.isDomain(termo)) {
                            validaDominio = false;
                        }
                    }
                }
                if (validaDominio) {
                    docs.add(doc);
                }
            }
            
            urls[0] = robo.getNextLink(termosConsulta);
            robo.visitedLink(urls[0]);
            urls[1] = robo.getNextLink(termosConsulta);

        } while (docs.size() <= 4);

        //IndexarDocumentos
        indexarDocumentos(robo);

        //Gerar Lista Invertida
        gerarListaInvertida(termosConsulta);
        
        //Ordenar itens da lista invertida por código
        ordenarItensListaInvertida(termosConsulta);
        
        //Merge da Lista Invertida
        List<Documento> documentos = mergeItens(termosConsulta);
        
        List<ResultadoBusca> resultBusca = new ArrayList<ResultadoBusca>();
        for(Documento doc: documentos){
            ResultadoBusca rs = new ResultadoBusca();
            rs.setDocumento(doc);
            rs.setScore(doc.calcularScore(termosConsulta, documentos.size(), documentos.size()));
            resultBusca.add(rs);
        }
        
        Collections.sort(resultBusca);
        
        for(ResultadoBusca rs: resultBusca){
            System.out.println("Documento codigo: " + rs.getDocumento().getCodigo() + "Score: " +rs.getScore());
        }
      
        
    } 
    private void indexarDocumentos(Robo robo){
        try {
            robo.indexarDocumentos(docs);
        } catch (SAXException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void gerarListaInvertida(List<String> termosConsulta){
    listaInvertida = new HashMap<String, List<Documento>>();

        List<Documento> itensListaInvertida;
        for(String termoBusca: termosConsulta){
            for (Documento doc : docs) {
                for (Termo termo : doc.getCentroide().getTermos()) {
                    if(termo.getTermo().equals(termoBusca)){
                        if (!listaInvertida.containsKey(termoBusca)) {
                            listaInvertida.put(termoBusca, new ArrayList<Documento>());
                    }
                    itensListaInvertida = listaInvertida.get(termoBusca);
                    itensListaInvertida.add(doc);
                    System.out.println("Documento " + doc.getCodigo() + " esta no Dominio");
                    }             
                }
                
            }
        }
}
    private void ordenarItensListaInvertida(List<String> termosConsulta){
        for (String chave : termosConsulta){
			if(termosConsulta != null){
                            List<Documento> itens = listaInvertida.get(chave);
                            Collections.sort(itens);
                                for(Documento item : itens){
                                    System.out.println("Chave: "+chave + " Codigo: "+item.getCodigo());                      
                                }
                        }
        }
    }
    
    private List<Documento> mergeItens(List<String> termosConsulta){
        
        List<Documento> itens = new ArrayList<Documento>();
        List<Documento> itens1 = listaInvertida.get(termosConsulta.get(0));
        List<Documento> itens2 = listaInvertida.get(termosConsulta.get(1));
        int i=0,j=0;
        do{
           if(itens1.get(i).getCodigo() == itens2.get(j).getCodigo()){
                    itens.add(itens1.get(i));
                    i++;
                    j++;
                }else if(itens1.get(i).getCodigo() > itens2.get(j).getCodigo()){
                    j++;
                }else if(itens1.get(i).getCodigo() < itens2.get(j).getCodigo()){
                    i++;
                } 
        }while(i < itens1.size() && j < itens2.size());
        
        for(Documento doc: itens){
            System.out.println("Documento codigo: " + doc.getCodigo());
        }
        return itens;
    }
    
    
    public List getLinksToSearch(List<String> palavras){
        List list = new ArrayList();
        for (String palavra : palavras) {
            if(listaInvertida.containsKey(palavra)){
                for(Documento item : listaInvertida.get(palavra)){
                    if(docs.contains(item.getCodigo()))
                        list.add(docs.get(docs.indexOf(item.getCodigo())));
                }                
            }
        }
        return list;
    } 

    
}
