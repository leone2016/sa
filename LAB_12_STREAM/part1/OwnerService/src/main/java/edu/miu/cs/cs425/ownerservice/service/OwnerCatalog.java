package edu.miu.cs.cs425.ownerservice.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.miu.cs.cs425.ownerservice.model.Owner;

@Service
public class OwnerCatalog {

    private final Map<String, Owner> owners = Map.ofEntries(
            Map.entry("AA1000", new Owner("AA1000", "Mia", "Johnson", "102 Elm Street")),
            Map.entry("AA1001", new Owner("AA1001", "Oscar", "Green", "55 Oak Avenue")),
            Map.entry("BB1000", new Owner("BB1000", "Liam", "Turner", "19 River Road")),
            Map.entry("BB1001", new Owner("BB1001", "Ava", "Sanchez", "78 Cedar Court")),
            Map.entry("CC1000", new Owner("CC1000", "Noah", "Baker", "12 Pine Lane")),
            Map.entry("DD1000", new Owner("DD1000", "Emma", "Lee", "45 Maple Drive")),
            Map.entry("EE1000", new Owner("EE1000", "Lucas", "Nguyen", "33 Willow Way")),
            Map.entry("FF1000", new Owner("FF1000", "Sophia", "Kim", "88 Birch Boulevard")),
            Map.entry("GA1000", new Owner("GA1000", "Ethan", "Ross", "150 Forest Trail")),
            Map.entry("FB1000", new Owner("FB1000", "Amelia", "Patel", "60 Lakeview Terrace")),
            Map.entry("FA1000", new Owner("FA1000", "Oliver", "Adams", "201 Summit Street")),
            Map.entry("FG1000", new Owner("FG1000", "Isabella", "Clark", "3 Meadow Court"))
    );

    public Optional<Owner> findOwnerByLicencePlate(String licencePlate) {
        return Optional.ofNullable(owners.get(licencePlate));
    }
}

