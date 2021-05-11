package com.onea.sidot.gestioneau;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.onea.sidot.gestioneau");

        noClasses()
            .that()
            .resideInAnyPackage("com.onea.sidot.gestioneau.service..")
            .or()
            .resideInAnyPackage("com.onea.sidot.gestioneau.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.onea.sidot.gestioneau.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
