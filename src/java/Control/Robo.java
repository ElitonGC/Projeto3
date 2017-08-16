package Control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Model.Documento;
import Model.Seed;

public class Robo {

	private XML xml = new XML();

	public String getNextLink(List<String> consulta) {

		return xml.getNextLink(consulta);
	}

	public void visitedLink(String link) {
		xml.visitedLink(link);
	}

	public void addLinks(List<Seed> seeds) {
		xml.addLinks(seeds);
	}

	public List<Parser> baixarPaginaSimultaneo(String[] url, int qtd) {

		List<Parser> parsers = new ArrayList<Parser>();
		Thread thread = null;

		try {
			for (int i = 0; i < qtd; i++) {
				Parser parser = new Parser(url[i], xml);
				thread = new Thread(parser);
				thread.start();
				parsers.add(parser);
			}
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parsers;
	}

	public List<Documento> carregarPaginas() {

		return xml.carregarPaginas();
	}

	public void indexarDocumentos(List<Documento> docs) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		xml.indexarDocumentos(docs);
	}

	public List<Documento> carregarPaginasDominio(String termo) {
		// TODO Auto-generated method stub
		return xml.carregarPaginasDominio(termo);
	}

}
