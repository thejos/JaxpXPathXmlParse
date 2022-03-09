package jaxpxpathxmlparse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
public class JaxpXPathXmlParseApp {

    //expression filters out book/s by price higher than 10 and by publishing date: after 31. december 2005.
    private static final String EXPRESSION = "//book [price>10.00 and number(translate(publish_date,'-','')) > 20051231]";
    private static XPathExpression xpExpression;
    private static InputSource inputSource;
    private static Object xPathResult;

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {

        XPathFactory xpFactory = XPathFactory.newInstance();
        XPath xPath = xpFactory.newXPath();

        //compiles an XPath expression
        try {
            xpExpression = xPath.compile(EXPRESSION);
        } catch (XPathExpressionException xpeExc) {
            System.out.println("XPathExpression compile failed... " + xpeExc.getMessage());
        }

        //defines a pathname of a file to be opened for reading
        File xmlDoc = new File(".\\src\\jaxpxpathxmlparse\\BookCatalog.xml");

        //creates new input source
        try {
            inputSource = new InputSource(new FileInputStream(xmlDoc));
        } catch (FileNotFoundException fnfExc) {
            System.out.println("File does not exist... " + fnfExc.getMessage());
        }

        //evaluates the compiled XPath expression over the InputSource document
        try {
            xPathResult = xpExpression.evaluate(inputSource, XPathConstants.NODESET);
        } catch (XPathExpressionException xpeExc) {
            System.out.println("Expression cannot be evaluated... " + xpeExc.getMessage());
        }

        //cast the result of evaluation to type NodeList
        NodeList nodeList = (NodeList) xPathResult;

        System.out.println("Books published since 01/01/2006 with price higher than 10.00");
        System.out.println("-------------------------------------------------------------\n"
                + "matches found: " + nodeList.getLength()
                + "\n----------------\n");

        //read the elements of the NodeList collection
        for (int i = 0; i < nodeList.getLength(); i++) {

            //fetch the attributes of a NodeList element; in case of this app there is only one attribute;
            System.out.println(nodeList.item(i).getAttributes().item(0).getNodeName() + ": " + nodeList.item(i).getAttributes().item(0).getTextContent());
            //fetch child nodes, cast nodes to type Element, store nodes to NodeList type collection
            NodeList childNodes = ((Element) nodeList.item(i)).getChildNodes();

            //read the elements of the NodeList collection
            for (int j = 0; j < childNodes.getLength(); j++) {

                Node childNode = childNodes.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(childNode.getNodeName() + ": " + childNode.getTextContent());
                }

            }
            System.out.println();

        }//for loop END

    }//main() END

}
