package at.fhtw.batch.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlReader {

    public static <T> T unmarshal(Class<T> clazz, Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return clazz.cast(unmarshaller.unmarshal(reader));
        } catch (JAXBException | IOException e) {
            // todo: handle exception
            throw new RuntimeException(e);
        }
    }
}
