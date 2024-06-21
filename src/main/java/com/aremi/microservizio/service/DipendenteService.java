package com.aremi.microservizio.service;

import com.aremi.microservizio.dto.GenericResponse;
import com.aremi.microservizio.dto.GetByIdRequest;
import com.aremi.microservizio.dto.GetDipendenteResponse;
import com.aremi.microservizio.dto.bean.DipendenteBean;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.soap.SOAPException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringResult;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class DipendenteService extends WebServiceGatewaySupport {
    private final Logger logger;
    private final Jaxb2Marshaller marshaller;
    public DipendenteService() {
        logger = Logger.getLogger("DipendenteService_Logger");

        marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.aremi.microservizio.dto"); //Serve per dichiarare a JAXB e al marshaller dove troverà l'ObjectFactory e le classi Request/Respose che mapperà

        // Imposta il marshaller e l'unmarshaller per il WebServiceTemplate
        getWebServiceTemplate().setMarshaller(marshaller); // Marshaller: xml to java-class
        getWebServiceTemplate().setUnmarshaller(marshaller); // Unmarshaller: java-class to xml
    }

    public GenericResponse<DipendenteBean> getDipendenteBeanById(Long id) {
        logger.info("DipendenteService::getDipendenteBeanById started...");
        GetByIdRequest request = new GetByIdRequest();
        request.setId(id);

        QName qName = new QName("http://example/infrastructure/sas-simulation-webservice", "getDipendenteRequest");
        JAXBElement<GetByIdRequest> jaxbElement = new JAXBElement<>(qName, GetByIdRequest.class, request);

        logger.info("DipendenteService::getDipendenteBeanById sending soap request ID:" + request.getId());

/*        // Stringo e mappo l'xml della request in una stringa e la stampo sul log per verifica
        try {
            StringResult result = new StringResult();
            getWebServiceTemplate().getMarshaller().marshal(jaxbElement, result);
            logger.info("DipendenteService::getDipendenteBeanById XML Request body:\n" + result);
        } catch (XmlMappingException | IOException e) {
            logger.info("DipendenteService::getDipendenteBeanById errore:\n" + e.getMessage());
        }*/

        // Effettua la chiamata SOAP
        GetDipendenteResponse response = (GetDipendenteResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://simulatore-sas:8081/ws", jaxbElement,
                        new SoapActionCallback("http://simulatore-sas:8081/ws"));

        // TODO: Converte la risposta SOAP in un oggetto DipendenteBean
        logger.info("Risposa da SAS:\n" + response);
        return null;
    }
}
