package Control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Model.Centroide;
import Model.Documento;
import Model.Seed;
import Model.Termo;

public class XML {

	private DocumentBuilderFactory documentBuilderFactoryS = DocumentBuilderFactory.newInstance();
	private DocumentBuilder documentBuilderS;
	private Document documentS;
	private Element rootS;
	private StreamResult resultS = new StreamResult(new File("seeds.xml"));

	DocumentBuilderFactory documentBuilderFactoryC = DocumentBuilderFactory.newInstance();

	DocumentBuilder documentBuilderC;
	Document documentC;
	private Element rootC;
	StreamResult resultC = new StreamResult(new File("parser.xml"));

	public XML() {
		try {

			this.documentBuilderS = documentBuilderFactoryS.newDocumentBuilder();
			this.documentS = documentBuilderS.newDocument();
			this.rootS = documentS.createElement("parser");
			documentS.appendChild(rootS);

			this.documentBuilderC = documentBuilderFactoryS.newDocumentBuilder();
			this.documentC = documentBuilderC.newDocument();
			this.rootC = documentC.createElement("parser");
			documentC.appendChild(rootC);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void gerarXML(Parser parser) throws IOException, TransformerException, ParserConfigurationException {

		Centroide centroide = parser.getCentroide();

		Element documento = documentC.createElement("document");
		rootC.appendChild(documento);

		Attr codigo = documentC.createAttribute("codigo");

		Attr title = documentC.createAttribute("title");
		title.appendChild(documentC.createTextNode(parser.getTitle()));

		Attr qtdTermos = documentC.createAttribute("qtdTermos");
		qtdTermos.appendChild(documentC.createTextNode(String.valueOf(parser.getQtdTermos())));

		Attr qtdTermosDif = documentC.createAttribute("qtdTermosDif");
		qtdTermosDif.appendChild(documentC.createTextNode(String.valueOf(parser.getQtdTermosDiferentes())));

		Attr link = documentC.createAttribute("link");
		link.appendChild(documentC.createTextNode(String.valueOf(parser.getLink())));

		documento.setAttributeNode(codigo);
		documento.setAttributeNode(title);
		documento.setAttributeNode(qtdTermos);
		documento.setAttributeNode(qtdTermosDif);
		documento.setAttributeNode(link);

		List<Termo> termos = centroide.getTermos();

		for (Termo termo : termos) {
			Element newTermo = documentC.createElement("termo");

			Attr value = documentC.createAttribute("value");
			value.appendChild(documentC.createTextNode(termo.getTermo()));

			Attr peso = documentC.createAttribute("peso");
			peso.appendChild(documentC.createTextNode(String.valueOf(termo.getPeso())));

			Attr quantidade = documentC.createAttribute("quantidade");
			quantidade.appendChild(documentC.createTextNode(String.valueOf(termo.getQuantidade())));

			newTermo.setAttributeNode(value);
			newTermo.setAttributeNode(peso);
			newTermo.setAttributeNode(quantidade);

			documento.appendChild(newTermo);
		}

		DOMSource source = new DOMSource(documentC);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(source, resultC);

	}

	public void addLinks(List<Seed> seeds) {
		try {

			Element documento = documentS.createElement("document");
			rootS.appendChild(documento);

			NodeList list = rootS.getElementsByTagName("url");
			for (int i = 0; i < list.getLength(); i++) {
				if (seeds.remove(new Seed(list.item(i).getTextContent(), false))) {
					// System.out.println("Remo��o de duplicidade do link: " +
					// list.item(i).getTextContent());
				}
			}

			for (Seed seed : seeds) {
				Element newSeed = documentS.createElement("seed");

				Element url = documentS.createElement("url");
				url.appendChild(documentS.createTextNode(seed.getUrl()));
				Element visited = documentS.createElement("visited");
				visited.appendChild(documentS.createTextNode(String.valueOf(seed.isVisited())));

				newSeed.appendChild(url);
				newSeed.appendChild(visited);

				documento.appendChild(newSeed);
			}

			DOMSource source = new DOMSource(documentS);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(source, resultS);

		} catch (Exception e) {
			// System.out.println("Erro: " + e.getMessage());
			// e.printStackTrace();
		}
	}

	public String getNextLink(List<String> consulta) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(new File("seeds.xml"));

			Element root = document.getDocumentElement();

			NodeList list1 = root.getElementsByTagName("visited");
                        NodeList list2 = root.getElementsByTagName("url");
                        

			for (int i = 0; i < list1.getLength(); i++) {
                            for(String termo: consulta){
                               
                                if (!Boolean.valueOf(list1.item(i).getTextContent()) && list2.item(i).getTextContent().contains(termo)) {
					return list1.item(i).getParentNode().getFirstChild().getTextContent();
				}
                            }
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			// e.printStackTrace();
		}
		return new String();
	}

	public void visitedLink(String link) {
		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new File("seeds.xml"));
			Element root = document.getDocumentElement();
			NodeList list = root.getElementsByTagName("url");

			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getTextContent().equals(link)) {
					list.item(i).getParentNode().getLastChild().setTextContent("true");
					break;
				}
			}

			DOMSource source = new DOMSource(document);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(new File("seeds.xml"));
			transformer.transform(source, result);

		} catch (Exception e) {
			// System.out.println("Erro: " + e.getMessage());
			// e.printStackTrace();
		}
	}

	public List<Documento> carregarPaginas() {
		List<Documento> docs = new ArrayList<Documento>();

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(new File("parser.xml"));

			Element root = document.getDocumentElement();

			NodeList list = root.getElementsByTagName("document"); // Carrega
																	// lista de
																	// documentos

			for (int i = 0; i < list.getLength(); i++) {
				Documento doc = new Documento();
				NodeList list2 = list.item(i).getChildNodes(); // Carrega a
																// lista de
																// termos
				for (int j = 0; j < list2.getLength(); j++) { // Cada termo
					NamedNodeMap att = list2.item(j).getAttributes(); // Carregar
																		// lista
																		// de
																		// atributos

					// System.out.println("Atributo " +
					// att.item(2).getTextContent()); //
				}

			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}

	public void indexarDocumentos(List<Documento> docs) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new File("parser.xml"));
		Element root = document.getDocumentElement();

		int cod = 0;
		for (Documento documento : docs) {
			cod++;
			documento.setCodigo(cod);
		}

		cod = 0;
		NodeList list = root.getElementsByTagName("document");
		for (int i = 0; i < list.getLength(); i++) {
			cod++;
			// docs.get(i).setCodigo(cod);
			Element e = (Element) list.item(i);

			Attr codigo = e.getAttributeNode("codigo");
			codigo.setValue(String.valueOf(cod));
			// codigo.appendChild(documentC.createTextNode(String.valueOf(cod)));
			// e.setAttributeNode(codigo);
		}

		DOMSource source = new DOMSource(document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result = new StreamResult(new File("parser.xml"));
		transformer.transform(source, result);
	}

	public List<Documento> carregarPaginasDominio(String dominio) {

		List<Integer> indicesDocsDomain = new ArrayList<Integer>();

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(new File("parser.xml"));

			Element root = document.getDocumentElement();

			NodeList list = root.getElementsByTagName("document"); // Carrega
																	// lista de
																	// documentos

			for (int i = 0; i < list.getLength(); i++) {
				Documento doc = new Documento();
				NodeList list2 = list.item(i).getChildNodes(); // Carrega a
																// lista de
																// termos
				for (int j = 0; j < list2.getLength(); j++) { // Cada termo
					NamedNodeMap att = list2.item(j).getAttributes(); // Carregar
																		// lista
																		// de
																		// atributos
					String termo = att.item(2).getTextContent();
					if (termo.equals(dominio)) {
						Element e = (Element) list.item(i);

						Attr codigo = e.getAttributeNode("codigo");
						int cod = Integer.valueOf(codigo.getValue());
						indicesDocsDomain.add(cod);
					} else {

					}
					// System.out.println("Atributo " +
					// att.item(2).getTextContent()); //
				}

			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}
}
