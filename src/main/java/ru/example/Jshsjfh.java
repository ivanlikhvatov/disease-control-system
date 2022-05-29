package ru.example;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.IOException;
import java.net.URL;

public class Jshsjfh {
    public static void main(String[] args) throws SOAPException, IOException {

        System.setProperty("java.net.useSystemProxies", "true");

//        sendMessageForApproveDisease();
        sendTestMessage();
    }

    private static void sendTestMessage() throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();

        SOAPBody body = message.getSOAPBody();
        QName bodyName = new QName("http://wombat.ztrade.com", "GetLastTradePrice", "m");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);

        QName name = new QName("symbol");
        SOAPElement symbol = bodyElement.addChildElement(name);
        symbol.addTextNode("SUNW");

        java.net.URL endpoint = new URL("http://wombat.ztrade.com/quotes");

        SOAPConnectionFactory soapConnectionFactory =
                SOAPConnectionFactory.newInstance();

        SOAPConnection connection = soapConnectionFactory.createConnection();

        SOAPMessage response = connection.call(message, endpoint);

        connection.close();

        response.writeTo(System.out);

        System.out.println();

    }

    private static void sendMessageForApproveDisease() throws SOAPException, IOException {
        //        MessageFactory factory =
//                MessageFactory.newInstance();
//
//        SOAPMessage message = factory.createMessage();
//
//        SOAPPart part = message.getSOAPPart();
//        SOAPEnvelope envelope = part.getEnvelope();
//        envelope.addAttribute(QName.valueOf("xmlns:asy"), "http://asystems.fss");
//
//
//        SOAPBody body = message.getSOAPBody();
//        QName bodyName = new QName("", "GetLastTradePrice", "asy");
//        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
//
//        QName name = new QName("id");
//        SOAPElement symbol = bodyElement.addChildElement(name);
//        symbol.addTextNode("2021-0102-08238-60-6604500683");
//
//        message.writeTo(System.out);
//        System.out.println();


        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "http://asystems.fss";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("asy", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(
                "UploadGetByExtID", "asy");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("id",
                "asy");
        soapBodyElem1.addTextNode("2021-0102-08238-60-6604500683");

//        MimeHeaders headers = soapMessage.getMimeHeaders();
//        headers.addHeader("SOAPAction", serverURI + "UploadGetByExtID");

        soapMessage.saveChanges();

        soapMessage.writeTo(System.out);
        System.out.println();


        SOAPConnectionFactory soapConnectionFactory =
                SOAPConnectionFactory.newInstance();

        SOAPConnection connection = soapConnectionFactory.createConnection();

        java.net.URL endpoint = new URL("http://asystems.fss");

        SOAPMessage response = connection.call(soapMessage, endpoint);
        response.writeTo(System.out);
        System.out.println();
    }
}



