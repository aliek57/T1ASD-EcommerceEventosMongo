package br.edu.utfpr.td.tsi.ecommerce.entrega;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntregaPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void notificarEmail(String emailJson) {
        rabbitTemplate.convertAndSend("fila.email", emailJson);
    }
    
    public void notificarCompraConcluida(String vendaJson) {
        rabbitTemplate.convertAndSend("fila.compra.concluida", vendaJson);
    }
}