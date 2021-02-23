package es.lojo.clickercompetition.demo;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE) //LE DECIMOS ERES TO IMPORTANT!
@ControllerAdvice //Clase para controlar excepciones
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    //Con esta anotación indicamos que metemos nueca escepción (entre los paréntesis indicamos tipo de ex)

    /**
     * Devuelve una excepción 404 NOT FOUND con un JSON en el cuerpo del mismo un id y el id que le pasabamos
     * a la excepción pasado a String
     * @param ex {String} : el id que le pasabamos a la excepción cuando haciamos que saltara
     * @return ResponseEntity : Respuesta HTTP con json en el cuerpo de este id
     */
    @ExceptionHandler(EntityNotFoundException.class) //mismo nombre que la excepción que lanzabamos en el put y delete cuando no encontraba el id
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>("id "+ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
