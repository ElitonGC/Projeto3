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
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Busca {

    private Map<String, List<ItemListaInvertida>> listaInvertida;
    private List<Documento> docs;
    //private List<Documento> documentos;

    public void executar() {
        docs = new ArrayList<Documento>();
        List<String> termosConsulta = new ArrayList<String>();

        termosConsulta.add("previdencia");
        termosConsulta.add("reforma");

        Boolean validaDominio = false;

        Robo robo = new Robo();
        String[] urls = new String[1];
        urls[0] = "http://g1.globo.com/politica/noticia/maia-diz-que-nao-ha-votos-para-aprovar-a-reforma-da-previdencia.ghtml";
        urls[0] = "http://www.reformadaprevidencia.gov.br/";
        //urls[2] = "http://noticias.r7.com/economia/reforma-da-previdencia";
        List<Parser> parsers;

        // for(int i = 0; i < parsers.size();i++){
        do {
            parsers = robo.baixarPaginaSimultaneo(urls, 1);
            for (int i = 0; i < parsers.size(); i++) {
                Parser parser = parsers.get(i);
                Documento doc = new Documento();
                doc.setCentroide(parser.getCentroide());
                doc.setLink(parser.getLink());
                doc.setQtdTermos(parser.getQtdTermos());
                doc.setQtdTermosDiferentes(parser.getQtdTermosDiferentes());
                doc.setSeeds(parser.getLinksUrl(parser.getLink()));
                doc.setTitle(parser.getTitle());
                doc.setCodigo(docs.size() + 1);

                robo.addLinks(doc.getSeeds());
                robo.visitedLink(doc.getLink());

                validaDominio = true;
                if (doc.getTitle().contains(termosConsulta.get(0))) {
                    for (String termo : termosConsulta) {
                        if (!doc.isDomain(termo)) {
                            validaDominio = false;
                        }
                    }
                }
                if (validaDominio) {
                    docs.add(doc);

                    try {
                        //IndexarDocumentos
                        //indexarDocumentos(robo);
                        robo.indexarDocumentos(docs);
                    } catch (IOException | ParserConfigurationException | TransformerException | SAXException ex) {
                        Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Gerar Lista Invertida
                    gerarListaInvertida(termosConsulta);

                    //Ordenar itens da lista invertida por código
                    ordenarItensListaInvertida(termosConsulta);

                    //Merge da Lista Invertida
                    //documentos = mergeItens(termosConsulta);
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                                new File("C:\\Testes\\listaInvertida.dat")));
                        out.writeObject(listaInvertida);
                        out.flush();
                        out.close();

                        out = new ObjectOutputStream(new FileOutputStream(new File("C:\\Testes\\docsBusca.dat")));
                        out.writeObject(docs);
                        out.flush();
                        out.close();

                        //out = new ObjectOutputStream(new FileOutputStream(new File("C:\\Testes\\documentosBusca.dat")));
                        //ut.writeObject(documentos);
                        //out.flush();
                        //out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            urls[0] = robo.getNextLink(termosConsulta);
            robo.visitedLink(urls[0]);
            //urls[1] = robo.getNextLink(termosConsulta);

        } while (docs.size() <= 10);
    }

    private void indexarDocumentos(Robo robo) {
        try {
            robo.indexarDocumentos(docs);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Busca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gerarListaInvertida(List<String> termosConsulta) {
        listaInvertida = new HashMap<String, List<ItemListaInvertida>>();

        List<ItemListaInvertida> itensListaInvertida;

        for (Documento doc : docs) {
            for (Termo termo : doc.getCentroide().getTermos()) {
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
    }

    private void ordenarItensListaInvertida(List<String> termosConsulta) {
        for (String chave : termosConsulta) {
            if (termosConsulta != null) {
                List<ItemListaInvertida> itens = listaInvertida.get(chave);
                Collections.sort(itens);
            }
        }
    }

    public List getLinksToSearch(List<String> palavras) {
        List list = new ArrayList();
        for (String palavra : palavras) {
            if (listaInvertida.containsKey(palavra)) {
                for (ItemListaInvertida item : listaInvertida.get(palavra)) {
                    if (docs.contains(item.getCod())) {
                        list.add(docs.get(docs.indexOf(item.getCod())));
                    }
                }
            }
        }
        return list;
    }

}
