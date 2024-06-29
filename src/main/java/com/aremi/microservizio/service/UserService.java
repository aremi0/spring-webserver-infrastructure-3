package com.aremi.microservizio.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.logging.Logger;

@Service
public class UserService extends WebServiceGatewaySupport implements UserDetailsService {
    private final Logger logger;
    private final Jaxb2Marshaller marshaller;
    private final ModelMapper modelMapper;

    public UserService() {
        logger = Logger.getLogger("UserService_Logger");

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
        return null;
    }
}
