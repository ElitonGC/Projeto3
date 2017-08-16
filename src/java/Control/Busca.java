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
import Model.ItemListaInvertida;
import Model.Termo;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Busca{

    private Map<String, List<ItemListaInvertida>> listaInvertida;
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

        listaInvertida = new HashMap<String, List<ItemListaInvertida>>();

        List<ItemListaInvertida> itensListaInvertida;
        for(String termoBusca: termosConsulta){
            for (Documento doc : docs) {
                for (Termo termo : doc.getCentroide().getTermos()) {
                    if(termoBusca.equals(termo.getTermo())){
                        if (!listaInvertida.containsKey(termo.getTermo())) {
                            listaInvertida.put(termo.getTermo(), new ArrayList<ItemListaInvertida>());
                    }
                    itensListaInvertida = listaInvertida.get(termo.getTermo());
                    ItemListaInvertida item = new ItemListaInvertida();
                    item.setCod(doc.getCodigo());
                    item.setQdt(termo.getQuantidade());
                    itensListaInvertida.add(item);
                    }             
                }
                System.out.println("Documento " + doc.getCodigo() + " esta no Dominio");
            }
        }
        
        
        //Ordenar itens da lista invertida por código
        for (String chave : termosConsulta){
			if(termosConsulta != null){
                            List<ItemListaInvertida> itens = listaInvertida.get(chave);
                            Collections.sort(itens);
                                for (ItemListaInvertida item : itens){
                                    System.out.println("Chave: "+chave + "Codigo: "+item.getCod());                      
                                }
                        }
		}
        
    }   
    
    public List getLinksToSearch(List<String> palavras){
        List list = new ArrayList();
        for (String palavra : palavras) {
            if(listaInvertida.containsKey(palavra)){
                for(ItemListaInvertida item : listaInvertida.get(palavra)){
                    if(docs.contains(item.getCod()))
                        list.add(docs.get(docs.indexOf(item.getCod())));
                }                
            }
        }
        return list;
    } 

    
}
