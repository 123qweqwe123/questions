package TextXml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
/*import org.w3c.dom.Document;
import org.w3c.dom.Element;*/
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class Mian {
	
	public static void main(String[] args) throws Exception {
		File file = new File("D:/JDOM.xml");
		//Mian.DOMcreate(file);
		Mian.JDOMcreate(file);
	}
	
	
	
	/**
	 * @author 
	 * 
	 * SAX 解析XML文档
	 */
	/*public class SaxDemo implements XmlDocument {

	    public void parserXml(String fileName) {
	        SAXParserFactory saxfac = SAXParserFactory.newInstance();

	        try {
	            SAXParser saxparser = saxfac.newSAXParser();
	            InputStream is = new FileInputStream(fileName);
	            saxparser.parse(is, new MySAXHandler());
	        } catch (ParserConfigurationException e) {
	            e.printStackTrace();
	        } catch (SAXException e) {
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	class MySAXHandler extends DefaultHandler {
	    boolean hasAttribute = false;
	    Attributes attributes = null;

	    public void startDocument() throws SAXException {
	        // System.out.println("文档开始打印了");
	    }

	    public void endDocument() throws SAXException {
	        // System.out.println("文档打印结束了");
	    }

	    public void startElement(String uri, String localName, String qName,
	            Attributes attributes) throws SAXException {
	        if (qName.equals("users")) {
	            return;
	        }
	        if (qName.equals("user")) {
	            return;
	        }
	        if (attributes.getLength() > 0) {
	            this.attributes = attributes;
	            this.hasAttribute = true;
	        }
	    }

	    public void endElement(String uri, String localName, String qName)
	            throws SAXException {
	        if (hasAttribute && (attributes != null)) {
	            for (int i = 0; i < attributes.getLength(); i++) {
	                System.out.print(attributes.getQName(0) + ":"
	                        + attributes.getValue(0));
	            }
	        }
	    }

	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
	        System.out.print(new String(ch, start, length));
	    }
	}*/
	
	
	
	
	/**
	 * @author 
	 * 
	 * Dom4j 解析XML文档
	 */
	 public void parserXml(String fileName) {
	        File inputXml = new File(fileName);
	        SAXReader saxReader = new SAXReader();

	        try {
	            Document document = saxReader.read(inputXml);
	            Element users = document.getRootElement();
	            for (Iterator i = users.elementIterator(); i.hasNext();) {
	                Element user = (Element) i.next();
	                for (Iterator j = user.elementIterator(); j.hasNext();) {
	                    Element node = (Element) j.next();
	                    System.out.println(node.getName() + ":" + node.getText());
	                }
	                System.out.println();
	            }
	        } catch (DocumentException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	
	
	/**
	 * @author 
	 * 
	 * JDOM 解析XML文档
	 * 
	 */
	public class JDomDemo  {

	    public void parserXml(String fileName) {
	        SAXBuilder builder = new SAXBuilder();

	        try {
	            Document document = builder.build(fileName);
	            org.jdom2.Element users = document.getRootElement();
	            List userList = users.getChildren("user");

	            for (int i = 0; i < userList.size(); i++) {
	                Element user = (Element) userList.get(i);
	                List userInfo = user.getChildren();

	                for (int j = 0; j < userInfo.size(); j++) {
	                    System.out.println(((Element) userInfo.get(j)).getName()
	                            + ":" + ((Element) userInfo.get(j)).getValue());

	                }
	                System.out.println();
	            }
	        } catch (JDOMException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }
	}
	
	
	
	/**
	 * @author 
	 * 
	 * DOM 解析XML文档
	 */
	/*public class DomDemo  {
	    

	    public void parserXml(String fileName) {
	        try {
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document document = db.parse(fileName);
	            NodeList users = document.getChildNodes();
	            
	            for (int i = 0; i < users.getLength(); i++) {
	                Node user = users.item(i);
	                NodeList userInfo = user.getChildNodes();
	                
	                for (int j = 0; j < userInfo.getLength(); j++) {
	                    Node node = userInfo.item(j);
	                    NodeList userMeta = node.getChildNodes();
	                    
	                    for (int k = 0; k < userMeta.getLength(); k++) {
	                        if(userMeta.item(k).getNodeName() != "#text")
	                            System.out.println(userMeta.item(k).getNodeName()
	                                    + ":" + userMeta.item(k).getTextContent());
	                    }
	                    
	                    System.out.println();
	                }
	            }
	            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (ParserConfigurationException e) {
	            e.printStackTrace();
	        } catch (SAXException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}*/
	
	
	
	
	public static void JDOMcreate(File file)throws Exception{
        org.jdom2.Document document=new org.jdom2.Document();
        org.jdom2.Element root=new org.jdom2.Element("rss");
        root.setAttribute("version", "2.0");
        root.addContent(
                new org.jdom2.Element("channel").addContent(
                        new org.jdom2.Element("title").setText("新闻国内")));
        document.setRootElement(root);
        XMLOutputter outputter=new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());//设置文本格式
        outputter.output(document, new FileOutputStream(file));
    }
	
	
	public static void DOM4Jcreate(File file)throws Exception{
        org.dom4j.Document document=DocumentHelper.createDocument();
        org.dom4j.Element root=document.addElement("rss");
        root.addAttribute("version", "2.0");
        org.dom4j.Element channel=root.addElement("channel");
        org.dom4j.Element title=channel.addElement("title");
        org.dom4j.Element conent=channel.addElement("context");
        conent.setText("heheheh");
        title.setText("新闻国内");
        XMLWriter writer=new XMLWriter(new FileOutputStream(file),OutputFormat.createPrettyPrint());
        writer.setEscapeText(false);//字符是否转义,默认true
        writer.write(document);
        writer.close();
    }
	
	
	
	
	public static void SAXcreate(File file)throws Exception {
        //初始化要生成文件的数据
        ArrayList<Student> students=new ArrayList<Student>();
        for (int i = 0; i < 3; i++) {
            Student student=new Student(i,"张"+i,5*i,10*i);
            students.add(student);
        }

        SAXTransformerFactory stf=(SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler=stf.newTransformerHandler();
        Transformer tf=handler.getTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");//标签自动换行
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");//编码格式
        StreamResult result=new StreamResult(file);//创建Result对象
        handler.setResult(result);//关联

        handler.startDocument();
        handler.startElement("", "", "tb_students", null);//根节点标签

        for (Student student : students) {
            AttributesImpl atts=new AttributesImpl();//创建熟悉
            atts.addAttribute("", "", "id", "", student.getId()+"");
            handler.startElement("", "", "student", atts);//元素标签开始

            handler.startElement("name", null, null, atts);//元素标签开始
            handler.characters(student.getName().toCharArray(), 0, student.getName().length());//元素标签内容
            handler.endElement("", "", "name");//元素标签结束

            handler.startElement("", "", "age", null);
            handler.characters((""+student.getAge()).toCharArray(), 0, (""+student.getAge()).length());
            handler.endElement("", "", "age");

            handler.startElement("", "", "grade", null);
            handler.characters((""+student.getGrade()).toCharArray(), 0,(""+student.getGrade()).toCharArray().length);
            handler.endElement("", "", "grade");

            handler.endElement("", "", "student");//元素标签结束
        }

        handler.endElement("", "", "tb_students");//结束根节点标签
        handler.endDocument();
    } 
		
	
	
	 /*public static void DOMcreate(File file)throws Exception{
	        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
	        DocumentBuilder db=dbf.newDocumentBuilder();
	        Document document=db.newDocument();
	        document.setXmlStandalone(true);
	        //document.setXmlVersion("2");
	        Element root=document.createElement("tb_student");
	        for (int i = 0; i < 3; i++) {
	            Element student=document.createElement("student");
	            Element name=document.createElement("name"),
	                    age=document.createElement("age"),
	                    grade=document.createElement("grade");
	                student.setAttribute("id", i+"");
	                name.setTextContent("张"+i);
	                age.setTextContent(""+i*5);
	                grade.setTextContent(""+i*20);
	                student.appendChild(name);
	                student.appendChild(age);
	                student.appendChild(grade);
	            root.appendChild(student);
	        }
	        document.appendChild(root);     
	        TransformerFactory tff=TransformerFactory.newInstance();
	        Transformer tf=tff.newTransformer();
	        tf.setOutputProperty(OutputKeys.INDENT, "yes");
	        tf.transform(new DOMSource(document), new StreamResult(file));
	    }*/

}
