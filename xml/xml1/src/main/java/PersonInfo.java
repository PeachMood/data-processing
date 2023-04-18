import java.util.HashSet;
import java.util.Set;

public class PersonInfo {
    public String id;
    public String firstName;
    public String lastName;
    public String gender;

    // parents
    public Set<String> parentsId = new HashSet<String>();
    public String motherId;
    public String fatherId;
    public Set<String> parentsName = new HashSet<String>();
    public String motherName;
    public String fatherName;

    // children
    public Set<String> childrenId = new HashSet<String>();
    public Set<String> sonsId = new HashSet<String>();
    public Set<String> daughtersId = new HashSet<String>();
    public Set<String> childrenName = new HashSet<String>();
    public Set<String> sonsName = new HashSet<String>();
    public Set<String> daughtersName = new HashSet<String>();

    // siblings
    public Set<String> siblingsId = new HashSet<String>();
    public Set<String> brothersId = new HashSet<String>();
    public Set<String> sistersId = new HashSet<String>();
    public Set<String> siblingsName = new HashSet<String>();
    public Set<String> brothersName = new HashSet<String>();
    public Set<String> sistersName = new HashSet<String>();

    // spouse
    public String spouseId;
    public String husbandId;
    public String wifeId;
    public String spouseName;
    public String husbandName;
    public String wifeName;

    public Integer childrenCount = null;
    public Integer siblingsCount = null;

    void merge(PersonInfo p) {
        if (p == null) {
            return;
        }

        if (id == null)
            id = p.id;


        if (firstName == null) firstName = p.firstName;
        if (lastName == null) lastName = p.lastName;
        if (gender == null) gender = p.gender;

        parentsId.addAll(p.parentsId);
        if (motherId == null) motherId = p.motherId;
        if (fatherId == null) fatherId = p.fatherId;
        parentsName.addAll(p.parentsName);
        if (motherName == null) motherName = p.motherName;
        if (fatherName == null) fatherName = p.fatherName;

        childrenId.addAll(p.childrenId);
        sonsId.addAll(p.sonsId);
        daughtersId.addAll(p.daughtersId);
        childrenName.addAll(p.childrenName);
        sonsName.addAll(p.sonsName);
        daughtersName.addAll(p.daughtersName);

        siblingsId.addAll(p.siblingsId);
        brothersId.addAll(p.brothersId);
        sistersId.addAll(p.sistersId);
        siblingsName.addAll(p.siblingsName);
        brothersName.addAll(p.brothersName);
        sistersName.addAll(p.sistersName);

        if (spouseId == null) spouseId = p.spouseId;
        if (husbandId == null) husbandId = p.husbandId;
        if (wifeId == null) wifeId = p.wifeId;
        if (spouseName == null) spouseName = p.spouseName;
        if (husbandName == null) husbandName = p.husbandName;
        if (wifeName == null) wifeName = p.wifeName;

        if (childrenCount == null) childrenCount = p.childrenCount;
        if (siblingsCount == null) siblingsCount = p.siblingsCount;
    }

    String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "ID: " + id + " " + firstName + " " + lastName + " " + gender +
                " | siblings: " + siblingsId.toString() + " " + sistersId.toString() + " " + brothersId +
                " | spouse: " + fatherId + " " + motherId + " " + parentsId +
                " | sn: " + siblingsCount + " cn: " + childrenCount;
    }
}
