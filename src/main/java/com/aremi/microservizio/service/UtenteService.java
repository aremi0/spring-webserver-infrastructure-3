package com.aremi.microservizio.service;

import com.aremi.microservizio.dto.jaxb.GetUtenteByEmailRequest;
import com.aremi.microservizio.dto.jaxb.GetUtenteResponse;
import com.aremi.microservizio.dto.jaxb.Utente;
import jakarta.xml.bind.JAXBElement;
import org.modelmapper.ModelMapper;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringResult;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UtenteService extends WebServiceGatewaySupport implements UserDetailsService {
    private final Logger logger;
    private final Jaxb2Marshaller marshaller;
    private final ModelMapper modelMapper;
   // private final PasswordEncoder passwordEncoder;

    public UtenteService() {
        logger = LoggerFactory.getLogger(UtenteService.class);
        //this.passwordEncoder = passwordEncoder;

        marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.aremi.microservizio.dto.jaxb"); //Serve per dichiarare a JAXB e al marshaller dove troverà l'ObjectFactory e le classi Request/Respose che mapperà

        // Imposta il marshaller e l'unmarshaller per il WebServiceTemplate
        getWebServiceTemplate().setMarshaller(marshaller); // Marshaller: xml to java-class
        getWebServiceTemplate().setUnmarshaller(marshaller); // Unmarshaller: java-class to xml

        // Imposto il modelMapper per mappare facilmente gli oggetti response in oggetti java
        modelMapper = new ModelMapper();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente user = null;
        List<GrantedAuthority> authoritiesList = null;

        logger.debug("UtenteService::loadUserByUsername starting to build the SOAP request");
        GetUtenteByEmailRequest request = new GetUtenteByEmailRequest();
        request.setEmailUtente(username);

        QName qName = new QName("http://example/infrastructure/sas-simulation-webservice", "getUtenteByEmailRequest");
        JAXBElement<GetUtenteByEmailRequest> jaxbElement = new JAXBElement<>(qName, GetUtenteByEmailRequest.class, request);

        logger.debug("UtenteService::loadUserByUsername sending SOAP to SAS with request Username/Email: " + request.getEmailUtente());

        // Stringo e mappo l'xml della request in una stringa e la stampo sul log per verifica
        try {
            StringResult result = new StringResult();
            getWebServiceTemplate().getMarshaller().marshal(jaxbElement, result);
            logger.debug("UtenteService::loadUserByUsername XML Request body:\n" + result);
        } catch (XmlMappingException | IOException e) {
            logger.debug("UtenteService::loadUserByUsername errore:\n" + e.getStackTrace());
        }

        // Effettua la chiamata SOAP
        GetUtenteResponse response = (GetUtenteResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://simulatore-sas:8081/ws", jaxbElement,
                        new SoapActionCallback("http://simulatore-sas:8081/ws"));

        logger.debug("UtenteService::loadUserByUsername response received from SAS:\n" + response);

        switch (response.getResponseDetail().getHttpCode()) {
            case 200, 201 -> {
                logger.debug("UtenteService::loadUserByUsername httpCode 200-201");
                String[] roles = response.getUtente().getAutorita().split(", ");
                if(response.getUtente().getAutorita().isEmpty() || response.getUtente().getAutorita().isBlank()) {
                    // caso in cui il DB mi ritorna ""
                    authoritiesList = Collections.singletonList(new SimpleGrantedAuthority("default"));
                } else {
                    // caso in cui il DB mi torna "valore1, valore2, ..." o anche "valore1"
                    authoritiesList = Arrays.stream(roles)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }
                user = modelMapper.map(response.getUtente(), Utente.class);
            }
            case 401 -> {
                //TODO: handle unhautorized
                logger.debug("UtenteService::loadUserByUsername httpCode 401");
            }
            case 500, 501, 502, 503, 504 -> {
                logger.debug("UtenteService::loadUserByUsername httpCode 500");
            }
        }

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Utente non trovato con username: " + username);
        } else {
            // Crea e restituisci un oggetto UserDetails
            return User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authoritiesList) // Aggiungi qui i ruoli o le autorità dell'utente
                    .build();
        }
    }
}
