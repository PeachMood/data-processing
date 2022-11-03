package task6;

import java.util.ArrayList;
import java.util.List;

public final class Company {
    private final List<Department> departments;

    public Company(final int departmentsCount) {
        this.departments = new ArrayList<>(departmentsCount);
        for (int i = 0; i < departmentsCount; i++) {
            departments.add(i, new Department(i));
        }
    }

    public void showCollaborativeResult() {
        System.out.println("All departments have completed their work.");
        final int result = departments.stream()
            .map(Department::getCalculationResult)
            .reduce(Integer::sum)
            .orElse(-1);
        System.out.println("The sum of all calculations is: " + result);
    }

    public int getDepartmentsCount() {
        return departments.size();
    }

    public Department getFreeDepartment(final int index) {
        return departments.get(index);
    }
}
