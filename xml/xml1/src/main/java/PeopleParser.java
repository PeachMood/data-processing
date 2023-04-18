import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PeopleParser {

    public ArrayList<PersonInfo> parse(InputStream stream) throws XMLStreamException {
        ArrayList<PersonInfo> data = new ArrayList<>();

        // XML reader init
        XMLInputFactory streamFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = streamFactory.createXMLStreamReader(stream);

        int peopleCount = 0;
        PersonInfo info = null;

        System.out.println("=== Start parsing... ===");

        while (reader.hasNext()) {
            reader.next();
            String[] temp;

            int event_type = reader.getEventType();
            switch (event_type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    switch (reader.getLocalName()) {
                        case "people":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("count")) {
                                    peopleCount = Integer.parseInt(reader.getAttributeValue(i));
                                    System.out.println("Total people count: " + peopleCount);
                                } else {
                                    System.out.println("Unknown attribute in <people>");
                                }
                            }
                            break;
                        case "person":
                            info = new PersonInfo();
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                switch (reader.getAttributeLocalName(i)) {
                                    case "name" -> {
                                        String[] full = reader.getAttributeValue(i).trim().split("\\s+");
                                        info.firstName = full[0];
                                        info.lastName = full[1];
                                    }
                                    case "id" -> {
                                        info.id = reader.getAttributeValue(i).trim();
                                    }
                                    default -> System.out.println("Unknown attribute in <person>");
                                }
                            }
                            break;
                        case "id":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    assert info != null;
                                    info.id = reader.getAttributeValue(i).trim();
                                } else {
                                    System.out.println("Unknown attribute in <id>");
                                }
                            }
                            break;
                        case "firstname":
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    if (reader.getAttributeLocalName(i).equals("value")) {
                                        assert info != null;
                                        info.firstName = reader.getAttributeValue(i).trim();
                                    } else {
                                        System.out.println("Unknown attribute in <firstname>");
                                    }
                                }
                            } else {
                                reader.next();
                                assert info != null;
                                info.firstName = reader.getText().trim();
                            }
                            break;
                        case "surname":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    assert info != null;
                                    info.lastName = reader.getAttributeValue(i).trim();
                                } else {
                                    System.out.println("Unknown attribute in <surname>");
                                }
                            }
                            break;
                        case "fullname":
                            break;
                        case "first":
                            reader.next();
                            assert info != null;
                            info.firstName = reader.getText().trim();
                            break;
                        case "family":
                        case "family-name":
                            reader.next();
                            assert info != null;
                            info.lastName = reader.getText().trim();
                            break;
                        case "gender":
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    if (reader.getAttributeLocalName(i).equals("value")) {
                                        assert info != null;
                                        info.gender = reader.getAttributeValue(i).trim().toUpperCase().substring(0, 1);
                                    } else {
                                        System.out.println("Unknown attribute in <gender>");
                                    }
                                }
                            } else {
                                reader.next();
                                assert info != null;
                                info.gender = reader.getText().trim().toUpperCase().substring(0, 1);
                            }
                            break;
                        case "spouce":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    if (!reader.getAttributeValue(i).trim().equals("NONE")) {
                                        assert info != null;
                                        info.spouseName = reader.getAttributeValue(i);
                                    }
                                } else {
                                    System.out.println("Unknown attribute in <spouce>");
                                }
                            }
                            if (reader.hasText()) {
                                if (!reader.getText().trim().equals("NONE")) {
                                    assert info != null;
                                    info.spouseName = reader.getText();
                                }
                            }
                            break;
                        case "husband":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    assert info != null;
                                    info.husbandId = reader.getAttributeValue(i).trim();
                                } else {
                                    System.out.println("Unknown attribute in <husband>");
                                }
                            }
                            break;
                        case "wife":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if ("value".equals(reader.getAttributeLocalName(i))) {
                                    assert info != null;
                                    info.wifeId = reader.getAttributeValue(i).trim();
                                } else {
                                    System.out.println("Unknown attribute in <wife>");
                                }
                            }
                            break;
                        case "siblings":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("val")) {
                                    List<String> siblings = Arrays.asList(reader.getAttributeValue(i).trim().split("\\s+"));
                                    assert info != null;
                                    info.siblingsId.addAll(siblings);
                                } else {
                                    System.out.println("Unknown attribute in <siblings>");
                                }
                            }
                            break;
                        case "brother":
                            reader.next();
                            temp = reader.getText().trim().split("\\s+");
                            assert info != null;
                            info.brothersName.add(temp[0] + " " + temp[1]);
                            break;
                        case "sister":
                            reader.next();
                            temp = reader.getText().trim().split("\\s+");
                            assert info != null;
                            info.sistersName.add(temp[0] + " " + temp[1]);
                            break;
                        case "siblings-number":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    assert info != null;
                                    info.siblingsCount = Integer.parseInt(reader.getAttributeValue(i).trim());
                                } else {
                                    System.out.println(reader.getLocalName() + " has unknown attribute: " + reader.getAttributeLocalName(i));
                                }
                            }
                            break;
                        case "children":
                            break;
                        case "child":
                            reader.next();
                            temp = reader.getText().trim().split("\\s+");
                            assert info != null;
                            info.childrenName.add(temp[0] + " " + temp[1]);
                            break;
                        case "son":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("id")) {
                                    assert info != null;
                                    info.sonsId.add(reader.getAttributeValue(i).trim());
                                } else {
                                    System.out.println("Unknown attribute in <son>");
                                }
                            }
                            break;
                        case "daughter":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("id")) {
                                    assert info != null;
                                    info.daughtersId.add(reader.getAttributeValue(i).trim());
                                } else {
                                    System.out.println("Unknown attribute in <daughter>");
                                }
                            }
                            break;
                        case "parent":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    if (!reader.getAttributeValue(i).trim().equals("UNKNOWN")) {
                                        assert info != null;
                                        info.parentsId.add(reader.getAttributeValue(i).trim());
                                    }
                                } else {
                                    System.out.println("Unknown attribute in <parent>");
                                }
                            }
                            break;
                        case "father":
                            reader.next();
                            temp = reader.getText().trim().split("\\s+");
                            assert info != null;
                            info.fatherName = temp[0] + " " + temp[1];
                            break;
                        case "mother":
                            reader.next();
                            temp = reader.getText().trim().split("\\s+");
                            assert info != null;
                            info.motherName = temp[0] + " " + temp[1];
                            break;
                        case "children-number":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    assert info != null;
                                    info.childrenCount = Integer.parseInt(reader.getAttributeValue(i).trim());
                                } else {
                                    System.out.println("Unknown attribute in <children-number>");
                                }
                            }
                            break;
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    if (reader.getLocalName().equals("person")) {
                        data.add(info);
                        info = null;
                    }
                }
                default -> {
                }
            }
        }

        reader.close();
        return normalize(data, peopleCount);
    }

    private ArrayList<PersonInfo> normalize(ArrayList<PersonInfo> data, Integer peopleCount) {
        HashMap<String, PersonInfo> id_records = new HashMap<>();
        ArrayList<PersonInfo> temp_records = new ArrayList<>();

        System.out.println("=== Normalizing ===");

        // fill up records with known id
        for (PersonInfo i : data) {
            if (i.id != null) {
                if (id_records.containsKey(i.id)) {
                    id_records.get(i.id).merge(i);
                } else {
                    id_records.put(i.id, i);
                }
            } else {
                temp_records.add(i);
            }
        }

        // check that number of ids is equal to number of people
        assert id_records.size() == peopleCount;
        // check that all id records have both first and last names
        assert id_records.values().parallelStream().allMatch(x -> x.firstName != null && x.lastName != null);
        assert temp_records.parallelStream().allMatch(x -> x.firstName != null && x.lastName != null);

        data = temp_records;
        temp_records = new ArrayList<>();

        // merge all people that hasn't namesakes
        for (PersonInfo p : data) {
            List<PersonInfo> found =  findInRecords(x -> x.firstName.equals(p.firstName) && x.lastName.equals(p.lastName),
                                            id_records.values());
            if (found.size() == 1) {
                PersonInfo foundPerson = found.get(0);
                foundPerson.merge(p);
                id_records.replace(foundPerson.id, foundPerson);
            }
        }

        data = temp_records;
        temp_records = new ArrayList<>();

        for (PersonInfo person : data) {
            if (person.siblingsId != null) {
                HashSet<String> siblings = new HashSet<>(person.siblingsId);
                List<PersonInfo> found = findInRecords(
                        x -> {
                            HashSet<String> xsib = new HashSet<>(x.siblingsId);
                            xsib.retainAll(siblings);
                            return xsib.size() > 0;
                        }, id_records.values()
                );
                if (found.size() == 1) {
                    found.get(0).merge(person);
                } else {
                    temp_records.add(person);
                }
            }
        }

        System.out.println("Normalized!!!");

        childrenAssertion(id_records);
        siblingsAssertion(id_records);
        genderAssertion(id_records);

        return new ArrayList<>(id_records.values());
    }

    private void childrenAssertion(HashMap<String, PersonInfo> id_records) {
        System.out.println("=== Children assertion ===");
        for (String key : id_records.keySet()) {
            PersonInfo p = id_records.get(key);
            p.childrenId.addAll(p.sonsId);
            p.childrenId.addAll(p.daughtersId);
            for (String s : p.daughtersName) {
                List<PersonInfo> f = findInRecords(x -> s.equals(x.firstName + " " + x.lastName),
                        id_records.values());
                if (f.size() >= 1) {
                    PersonInfo daughter = f.get(0);
                    if (daughter != null) {
                        p.childrenId.add(daughter.id);
                    }
                }
            }
            for (String s : p.sonsName) {
                List<PersonInfo> f = findInRecords(x -> s.equals(x.firstName + " " + x.lastName),
                        id_records.values());
                if (f.size() >= 1) {
                    PersonInfo son = f.get(0);
                    if (son != null) {
                        p.childrenId.add(son.id);
                    }
                }
            }
            for (String s : p.childrenName) {
                List<PersonInfo> f = findInRecords(x -> s.equals(x.firstName + " " + x.lastName),
                        id_records.values());
                if (f.size() >= 1) {
                    PersonInfo child = f.get(0);
                    if (child != null) {
                        p.childrenId.add(child.id);
                    }
                }
            }

            if (p.childrenCount != null) {
                if (!p.childrenCount.equals(p.childrenId.size())) {
                    System.out.println(p.firstName + " " + p.lastName + " " + p.childrenId.size() + " " + p.childrenCount);
                }
                try {
                    assert p.childrenId.size() == p.childrenCount;
                } catch (AssertionError e) {
                    System.out.println("CHILDREN ASSERTION FAILED: in " + p);
                }
            }
        }
        System.out.println("Children assertion finished!");
    }

    private void siblingsAssertion(HashMap<String, PersonInfo> id_records) {
        System.out.println("=== Siblings assertion ===");
        for (String key : id_records.keySet()) {
            PersonInfo p = id_records.get(key);
            p.siblingsId.addAll(p.brothersId);
            p.siblingsId.addAll(p.sistersId);
            for (String s : p.sistersName) {
                List<PersonInfo> f = findInRecords(x -> s.equals(x.firstName + " " + x.lastName),
                        id_records.values());
                if (f.size() >= 1) {
                    PersonInfo sister = f.get(0);
                    if (sister != null) {
                        p.siblingsId.add(sister.id);
                    }
                }
            }
            for (String s : p.brothersName) {
                List<PersonInfo> f = findInRecords(x -> s.equals(x.firstName + " " + x.lastName),
                        id_records.values());
                if (f.size() >= 1) {
                    PersonInfo brother = f.get(0);
                    if (brother != null) {
                        p.siblingsId.add(brother.id);
                    }
                }
            }
            for (String s : p.siblingsName) {
                List<PersonInfo> f = findInRecords(x -> s.equals(x.firstName + " " + x.lastName),
                        id_records.values());
                if (f.size() >= 1) {
                    PersonInfo sibling = f.get(0);
                    if (sibling != null) {
                        p.siblingsId.add(sibling.id);
                    }
                }
            }

            if (p.siblingsCount != null) {
                try {
                    assert p.siblingsId.size() == p.siblingsCount;
                } catch (AssertionError e) {
                    System.out.println("SIBLINGS ASSERTION FAILED: in " + p);
                }
            }
        }
        System.out.println("Siblings assertion finished!");
    }

    private void genderAssertion(HashMap<String, PersonInfo> id_records) {
        System.out.println("=== Gender assertion ===");
        for (var p : id_records.values()) {
            if (p.gender == null) {
                if (p.wifeId != null || p.wifeName != null) {
                    p.gender = "M";
                }
                else if (p.husbandId != null || p.husbandName != null) {
                    p.gender = "F";
                }
                else if (p.spouseId != null) {
                    PersonInfo pp = id_records.get(p.spouseId);
                    if (pp.gender != null) {
                        if (pp.gender.equals("M")) {
                            p.gender = "F";
                        }
                        if (pp.gender.equals("F"))  {
                            p.gender = "M";
                        }
                    }
                    else if (pp.husbandName != null || pp.husbandId != null) {
                        p.gender = "M";
                    }
                    else if (pp.wifeName != null || pp.wifeId != null) {
                        p.gender = "F";
                    }
                }
                else {
                    p.gender = "M";
                }
            }
        }


        for (var p : id_records.values()) {
            try {
                assert p.gender != null && (p.gender.equals("M") || p.gender.equals("F"));
            } catch (AssertionError e) {
                System.out.println("This person hasn't gender: " + p);
            }
        }
        System.out.println("Gender assertion finished!");
    }

    private List<PersonInfo> findInRecords(Predicate<PersonInfo> pred, Collection<PersonInfo> coll) {
        return coll.
                parallelStream().
                filter(pred).
                collect(Collectors.toList());
    }
}
