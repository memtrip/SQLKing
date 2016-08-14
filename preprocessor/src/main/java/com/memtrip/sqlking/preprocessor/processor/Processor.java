package com.memtrip.sqlking.preprocessor.processor;

import com.google.auto.service.AutoService;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.Table;
import com.memtrip.sqlking.preprocessor.processor.data.Data;
import com.memtrip.sqlking.preprocessor.processor.data.parse.ParseAnnotations;
import com.memtrip.sqlking.preprocessor.processor.data.validator.MembersHaveGetterSettersValidator;
import com.memtrip.sqlking.preprocessor.processor.data.validator.TableNamesMustBeUniqueValidator;
import com.memtrip.sqlking.preprocessor.processor.freemarker.DataModel;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {
	private FreeMarker mFreeMarker;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		mFreeMarker = getFreeMarker();
		Context.createInstance(env);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(Table.class);

        if (elements != null && elements.size() > 0) {
            final String GENERATED_FILE_PACKAGE = "com.memtrip.sqlking.gen";
            final String GENERATED_FILE_PATH = "Q.java";
            final String GENERATED_FILE_NAME = "Q";

            Data data = ParseAnnotations.parse(elements);

            try {
                validate(data);
            } catch (ValidatorException e) {
                Context.getInstance().getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        e.getMessage(),
                        e.getElement()
                );

                return false;
            }

            try {
                String body = mFreeMarker.getMappedFileBodyFromTemplate(GENERATED_FILE_PATH, DataModel.create(data));
                createFile(GENERATED_FILE_PACKAGE, GENERATED_FILE_NAME, body);
            } catch (IOException | FormatterException e) {
                Context.getInstance().getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        e.getMessage()
                );
            }
        }

        return true;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> set = new HashSet<>();
		set.add(Table.class.getCanonicalName());
		set.add(Column.class.getCanonicalName());
		return set;
	}

    private void validate(Data data) throws ValidatorException {
        for (Validator validator : getValidators(data))
            validator.validate();

        if (mFreeMarker == null)
            throw new ValidatorException("FATAL ERROR: Could not create an instance of FreeMarker");
    }

    private Validator[] getValidators(Data data) {
        return new Validator[]{
                new MembersHaveGetterSettersValidator(data),
                new TableNamesMustBeUniqueValidator(data)
        };
    }

	private void createFile(String packageName, String name, String body) throws IOException, FormatterException {
        String nameWithPackage = packageName + "." + name;
        JavaFileObject jfo = Context.getInstance().getFiler().createSourceFile(nameWithPackage);

        Context.getInstance().getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "creating SQLKing Q.java source file at: " + jfo.toUri());

        String formattedSource = new Formatter().formatSource(body);

        Writer writer = jfo.openWriter();
        writer.append(formattedSource);
        writer.close();
	}

	private FreeMarker getFreeMarker() {
		try {
			return new FreeMarker();
		} catch (IOException e) {
			return null;
		}
	}
}