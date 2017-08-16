package Control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import Model.Centroide;
import Model.Seed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Parser implements Runnable {

	private String link;

	private String title;
	private Centroide centroide;
	private int qtdTermosDiferentes;
	private int qtdTermos = 0;

	private XML xml;

	public Parser(String link, XML xml) {
		this.link = link;
		this.centroide = new Centroide();
		this.xml = xml;
	}

	private void parserPagina() throws TransformerException, ParserConfigurationException {

		System.out.println("Link:" + link);
		int qtdTermos = 0;

		try {
			// System.setProperty("http.proxyHost", "10.65.16.2");
			// System.setProperty("http.proxyPort", "3128");
			Document doc = Jsoup.connect(link).get();
			Elements titulo = doc.select("title");
			this.title = padronizarConteudo(titulo.text());

			Queue<Node> nodes = new ArrayDeque<>();

			nodes.addAll(doc.childNodes());

			while (!nodes.isEmpty()) {
				Node n = nodes.remove();

				if (n instanceof TextNode && ((TextNode) n).text().trim().length() > 0) {
					String t = ((TextNode) n).text().trim();
					t = t.replaceAll("\\s{2,}", " ");
					if (!t.equals("")) {
						String[] conteudos = t.split(" ");
						for (int j = 0; j < conteudos.length; j++) {
							conteudos[j] = conteudos[j].toLowerCase();
							conteudos[j] = conteudos[j].replace("%", "%%");
							conteudos[j] = padronizarConteudo(conteudos[j]);
							if (!ehStopList(conteudos[j])) {
								qtdTermos++;
								String tag = n.parent().nodeName();
								centroide.armazenarTermo(conteudos[j], tag);
								// imprimirTermos();
								System.out.println(tag + " contains text: " + conteudos[j]);
							}
						}
					}

				} else {
					nodes.addAll(n.childNodes());
				}
			}
			this.qtdTermos = qtdTermos;
			if (centroide.getTermos().size() > 0) {
				this.qtdTermosDiferentes = centroide.getTermos().size();
			} else {
				this.qtdTermosDiferentes = 0;
			}

			xml.gerarXML(this);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean ehStopList(String palavra) {
		String texto = carregarStopList();
		String[] palavras = texto.split(" ");
		for (int i = 0; i < palavras.length; i++) {
			if (palavras[i].equals(palavra)) {
				return true;
			}
		}
		return false;
	}

	private String padronizarConteudo(String conteudo) {

		conteudo = conteudo.toLowerCase();

		conteudo = conteudo.replace("%", "%%");

		conteudo = conteudo.replace("ç", "c");
		conteudo = conteudo.replace("ã", "a");
		conteudo = conteudo.replace("á", "a");
		conteudo = conteudo.replace("à", "a");
		conteudo = conteudo.replace("ê", "e");
		conteudo = conteudo.replace("é", "e");
		conteudo = conteudo.replace("è", "e");
		conteudo = conteudo.replace("í", "i");
		conteudo = conteudo.replace("ì", "i");
		conteudo = conteudo.replace("ó", "o");
		conteudo = conteudo.replace("ò", "o");
		conteudo = conteudo.replace("õ", "o");
		conteudo = conteudo.replace("ô", "o");
		conteudo = conteudo.replace("ú", "u");
		conteudo = conteudo.replace("ù", "u");
		conteudo = conteudo.replace("mente", "");
		conteudo = conteudo.replace("íssimos", "");
		conteudo = conteudo.replace("íssimas", "");
		conteudo = conteudo.replace("íssimo", "");
		conteudo = conteudo.replace("íssima", "");
		conteudo = conteudo.replace("issimos", "");
		conteudo = conteudo.replace("issimas", "");
		conteudo = conteudo.replace("issimo", "");
		conteudo = conteudo.replace("issima", "");

		return conteudo;
	}

	private String carregarStopList() {
		String texto = "";
		String line;
		try {

			FileReader arq = new FileReader("stoplist.txt");
			BufferedReader lerArq = new BufferedReader(arq);
			while ((line = lerArq.readLine()) != null) {
				texto += line;
				texto += " ";
			}
			arq.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return texto;
	}

	public List<Seed> getLinksUrl(String link) {
		List<Seed> seeds = new ArrayList<Seed>();
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(link).get();

			org.jsoup.select.Elements links = doc.select("a[href]");
			for (org.jsoup.nodes.Element link1 : links) {
				if (!seeds.contains(new Seed(link1.attr("abs:href"), false))) {
					seeds.add(new Seed(link1.attr("abs:href"), false));
				}
			}

		} catch (Exception e) {

		}
		return seeds;
	}

	@Override
	public void run() {
		try {
			parserPagina();
		} catch (TransformerException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getLink() {
		return link;
	}

	public Centroide getCentroide() {
		return centroide;
	}

	public String getTitle() {
		return title;
	}

	public int getQtdTermosDiferentes() {
		return qtdTermosDiferentes;
	}

	public int getQtdTermos() {
		return qtdTermos;
	}

}
