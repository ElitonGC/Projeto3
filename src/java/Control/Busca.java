package Control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import models.Documento;
import models.ItemListaInvertida;
import models.Termo;
import parser.Parser;

public class Busca {

	public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException, SAXException {
		List<Documento> docs = new ArrayList<Documento>();
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
				for (String termo : termosConsulta) {
					if (!doc.isDomain(termo)) {
						validaDominio = false;
					}
				}
				if (validaDominio)
					docs.add(doc);
			}
			urls[0] = robo.getNextLink();
			robo.visitedLink(urls[0]);
			urls[1] = robo.getNextLink();

		} while (docs.size() < 20);

		robo.indexarDocumentos(docs);

		Map<String, List<ItemListaInvertida>> listaInvertida = new HashMap<String, List<ItemListaInvertida>>();

		List<ItemListaInvertida> itensListaInvertida;

		for (int i = 0; i < docs.size(); i++) {
			Documento doc = docs.get(i);
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

			System.out.println("Documento " + doc.getCodigo() + " esta no Dominio");

		}

	}

}
