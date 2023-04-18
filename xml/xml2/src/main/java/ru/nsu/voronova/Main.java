package ru.nsu.voronova;

import org.xml.sax.SAXException;
import ru.nsu.voronova.parser.PeopleParser;
import ru.nsu.voronova.parser.PersonInfo;
import ru.nsu.voronova.schema.*;
import ru.nsu.voronova.schema.PersonType;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<String, PersonInfo> parsedData = new HashMap<>();
    private static final Map<String, PersonType> collectedData = new HashMap<>();

    public static void main(String[] args) {
        try (InputStream stream = new FileInputStream("src/main/resources/ru/nsu/voronova/people.xml")) {
            parsedData.putAll(new PeopleParser().parse(stream));
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }

        People people = new People();
        for (var info : parsedData.values()) {
            PersonType person = new PersonType();
            setPersonInfo(person, info);
            collectedData.put(info.id, person);
        }

        for (var person : collectedData.values()) {
            setSpouse(person);
            setChildren(person);
            setParents(person);
            setSiblings(person);
        }

        people.getPerson().addAll(collectedData.values());

        try {
            JAXBContext jc = null;
            ClassLoader classLoader = People.class.getClassLoader();
            jc = JAXBContext.newInstance("ru.nsu.voronova.schema", classLoader);
            Marshaller writer = jc.createMarshaller();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaFile = new File("src/main/resources/ru/nsu/voronova/schema.xsd");
            writer.setSchema(schemaFactory.newSchema(schemaFile));
            writer.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            writer.marshal(people, new File("src/main/resources/ru/nsu/voronova/output.xml"));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static void setPersonInfo(PersonType person, PersonInfo info) {
        person.setId(info.id);
        person.setName(info.getFullName());
        person.setGender(GenderType.fromValue(info.gender));
    }

    private static void setSpouse(PersonType person) {
        var info = parsedData.get(person.getId());

        if (info.wifeId != null) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(info.wifeId));
            person.setWife(personRef);

        } else if (info.husbandId != null) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(info.husbandId));
            person.setHusband(personRef);
        }
    }

    private static void setChildren(PersonType person) {
        var info = parsedData.get(person.getId());
        ChildrenType childrenType = new ChildrenType();

        for (var daughterId : info.daughtersId) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(daughterId));
            childrenType.getDaughter().add(personRef);
        }
        for (var sonId : info.sonsId) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(sonId));
            childrenType.getSon().add(personRef);
        }
        person.getChildren().add(childrenType);
    }

    private static void setParents(PersonType person) {
        PersonInfo info = parsedData.get(person.getId());
        ParentsType parentsType = new ParentsType();

        if (info.motherId != null) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(info.motherId));
            parentsType.setMother(personRef);
        }
        if (info.fatherId != null) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(info.fatherId));
            parentsType.setFather(personRef);
        }

        person.getParents().add(parentsType);
    }

    private static void setSiblings(PersonType person) {
        var info = parsedData.get(person.getId());
        SiblingsType siblingsType = new SiblingsType();

        for (var sisterId : info.sistersId) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(sisterId));
            siblingsType.getSister().add(personRef);
        }
        for (var brotherId : info.brothersId) {
            PersonRef personRef = new PersonRef();
            personRef.setId(collectedData.get(brotherId));
            siblingsType.getBrother().add(personRef);
        }
        person.getSiblings().add(siblingsType);
    }
}
