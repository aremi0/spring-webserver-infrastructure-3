package com.aremi.microservizio.service;

import com.aremi.microservizio.dto.GenericResponse;
import com.aremi.microservizio.dto.jaxb.GetByIdRequest;
import com.aremi.microservizio.dto.jaxb.GetDipendentiByIdSedeResponse;
import com.aremi.microservizio.dto.jaxb.GetDipendenteResponse;
import com.aremi.microservizio.dto.bean.DipendenteBean;
import jakarta.xml.bind.JAXBElement;
import org.modelmapper.ModelMapper;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringResult;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class DipendenteService extends WebServiceGatewaySupport {
    private final Logger logger;
    private final Jaxb2Marshaller marshaller;
    private final ModelMapper modelMapper;
    public DipendenteService() {
        logger = Logger.getLogger("DipendenteService_Logger");

        marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.aremi.microservizio.dto.jaxb"); //Serve per dichiarare a JAXB e al marshaller dove troverà l'ObjectFactory e le classi Request/Respose che mapperà

        // Imposta il marshaller e l'unmarshaller per il WebServiceTemplate
        getWebServiceTemplate().setMarshaller(marshaller); // Marshaller: xml to java-class
        getWebServiceTemplate().setUnmarshaller(marshaller); // Unmarshaller: java-class to xml

        // Imposto il modelMapper per mappare facilmente gli oggetti response in oggetti java
        modelMapper = new ModelMapper();
    }

    public GenericResponse<DipendenteBean> getDipendenteBeanById(Long id) {
        logger.info("DipendenteService::getDipendenteBeanById service started...");
        GenericResponse<DipendenteBean> finalResponse = new GenericResponse<>();

        logger.info("DipendenteService::getDipendenteBeanById starting to build the SOAP request");
        GetByIdRequest request = new GetByIdRequest();
        request.setId(id);

        QName qName = new QName("http://example/infrastructure/sas-simulation-webservice", "getDipendenteRequest");
        JAXBElement<GetByIdRequest> jaxbElement = new JAXBElement<>(qName, GetByIdRequest.class, request);

        logger.info("DipendenteService::getDipendenteBeanById sending SOAP to SAS with request ID:" + request.getId());

        // Stringo e mappo l'xml della request in una stringa e la stampo sul log per verifica
        try {
            StringResult result = new StringResult();
            getWebServiceTemplate().getMarshaller().marshal(jaxbElement, result);
            logger.info("DipendenteService::getDipendenteBeanById XML Request body:\n" + result);
        } catch (XmlMappingException | IOException e) {
            logger.info("DipendenteService::getDipendenteBeanById errore:\n" + e.getStackTrace());
        }

        // Effettua la chiamata SOAP
        GetDipendenteResponse response = (GetDipendenteResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://simulatore-sas:8081/ws", jaxbElement,
                        new SoapActionCallback("http://simulatore-sas:8081/ws"));

        logger.info("DipendenteService::getDipendenteBeanById response received from SAS:\n" + response);

        switch (response.getResponseDetail().getHttpCode()) {
            case 200, 201 -> {
                logger.info("DipendenteService::getDipendenteBeanById httpCode 200-201");
                DipendenteBean dipendenteBean = modelMapper.map(response.getDipendente(), DipendenteBean.class);
                finalResponse.getEntities().add(dipendenteBean);
            }
            case 401 -> {
                //TODO: handle unhautorized
                logger.info("DipendenteService::getDipendenteBeanById httpCode 401");
            }
            case 500, 501, 502, 503, 504 -> {
                logger.info("DipendenteService::getDipendenteBeanById httpCode 500");
            }
        }

        finalResponse.setEntitiesNumber(response.getResponseDetail().getEntitiesNumber());
        finalResponse.setHttpCode(response.getResponseDetail().getHttpCode());
        finalResponse.setDescription(response.getResponseDetail().getDescription());

        logger.info("DipendenteService::getDipendenteBeanById finalResponse:\n" + finalResponse);
        return finalResponse;
    }

    public GenericResponse<DipendenteBean> getDipendentiBeanByIdSede(Long idSede) {
        logger.info("DipendenteService::getDipendentiBeanByIdSede service started...");
        GenericResponse<DipendenteBean> finalResponse = new GenericResponse<>();

        logger.info("DipendenteService::getDipendentiBeanByIdSede starting to build the SOAP request");
        GetByIdRequest request = new GetByIdRequest();
        request.setId(idSede);

        QName qName = new QName("http://example/infrastructure/sas-simulation-webservice", "getDipendentiByIdSedeRequest");
        JAXBElement<GetByIdRequest> jaxbElement = new JAXBElement<>(qName, GetByIdRequest.class, request);

        // Stringo e mappo l'xml della request in una stringa e la stampo sul log per verifica
        try {
            StringResult result = new StringResult();
            getWebServiceTemplate().getMarshaller().marshal(jaxbElement, result);
            logger.info("DipendenteService::getDipendentiBeanByIdSede XML Request body:\n" + result);
        } catch (XmlMappingException | IOException e) {
            logger.info("DipendenteService::getDipendentiBeanByIdSede errore:\n" + e.getStackTrace());
        }

        // Effettua la chiamata SOAP
        GetDipendentiByIdSedeResponse response = (GetDipendentiByIdSedeResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://simulatore-sas:8081/ws", jaxbElement,
                        new SoapActionCallback("http://simulatore-sas:8081/ws"));

        logger.info("DipendenteService::getDipendentiBeanByIdSede response received from SAS:\n" + response);

        switch (response.getResponseDetail().getHttpCode()) {
            case 200, 201 -> {
                logger.info("DipendenteService::getDipendentiBeanByIdSede httpCode 200-201");
                for (var entity : response.getDipendenti()) {
                    DipendenteBean tmp = modelMapper.map(entity, DipendenteBean.class);
                    finalResponse.getEntities().add(tmp);
                }
            }
            case 401 -> {
                //TODO: handle unhautorized
                logger.info("DipendenteService::getDipendentiBeanByIdSede httpCode 401");
            }
            case 500, 501, 502, 503, 504 -> {
                logger.info("DipendenteService::getDipendentiBeanByIdSede httpCode 500");
            }
        }

        finalResponse.setEntitiesNumber(response.getResponseDetail().getEntitiesNumber());
        finalResponse.setHttpCode(response.getResponseDetail().getHttpCode());
        finalResponse.setDescription(response.getResponseDetail().getDescription());

        logger.info("DipendenteService::getDipendentiBeanByIdSede finalResponse:\n" + finalResponse);
        return finalResponse;
    }
}
