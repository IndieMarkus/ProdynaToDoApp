package at.markus.lehr.prodynatodo;

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
            .importPackages("at.markus.lehr.prodynatodo");

        noClasses()
            .that()
            .resideInAnyPackage("at.markus.lehr.prodynatodo.service..")
            .or()
            .resideInAnyPackage("at.markus.lehr.prodynatodo.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..at.markus.lehr.prodynatodo.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
