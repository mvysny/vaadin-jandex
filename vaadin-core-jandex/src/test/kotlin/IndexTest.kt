import com.github.mvysny.dynatest.DynaTest
import org.jboss.jandex.*
import org.jboss.jandex.AnnotationTarget
import java.lang.RuntimeException
import java.net.URL
import kotlin.test.expect

class IndexTest : DynaTest({
    lateinit var index: Index
    beforeEach {
        val jandexIndexUrl: URL =
            Thread.currentThread().contextClassLoader.getResource("META-INF/jandex.idx") ?: throw RuntimeException("No META-INF/jandex.idx on classpath")
        index = jandexIndexUrl.openStream().use {
            IndexReader(it).read()
        }
    }

    test("smoke") {
        val classInfo: ClassInfo =
            index.getClassByName(DotName.createSimple("com.vaadin.flow.server.VaadinServlet"))!!
    }

    test("find all @NpmPackage") {
        val components: List<ClassInfo> = index
            .getAnnotations(DotName.createSimple("com.vaadin.flow.component.dependency.NpmPackage"))
            .filter { it.target().kind() == AnnotationTarget.Kind.CLASS }
            .map { it.target() }
            .filterIsInstance<ClassInfo>()
        expect(true, components.toString()) { components.size > 20 }
        expect(1, components.toString()) {
            components.filter { it.simpleName() == "GeneratedVaadinButton" } .size
        }
    }

    test("find all @WebListener") {
        val initializers: List<ClassInfo> = index
            .getAnnotations(DotName.createSimple("javax.servlet.annotation.WebListener"))
            .filter { it.target().kind() == AnnotationTarget.Kind.CLASS }
            .map { it.target() }
            .filterIsInstance<ClassInfo>()
        expect(true, initializers.toString()) { initializers.size >= 2 }
        expect(1, initializers.toString()) {
            initializers.filter { it.simpleName() == "DevModeInitializer" } .size
        }
    }

    test("find all @HandlesTypes") {
        val initializers: List<ClassInfo> = index
            .getAnnotations(DotName.createSimple("javax.servlet.annotation.HandlesTypes"))
            .filter { it.target().kind() == AnnotationTarget.Kind.CLASS }
            .map { it.target() }
            .filterIsInstance<ClassInfo>()
        expect(true, initializers.toString()) { initializers.size >= 5 }
        expect(1, initializers.toString()) {
            initializers.filter { it.simpleName() == "DevModeInitializer" } .size
        }
    }
})
