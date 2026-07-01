package br.edu.utfpr.td.tsi.ecommerce.fiscal;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FiscalPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void solicitarAgendamentoEntrega(String vendaJson) {
        rabbitTemplate.convertAndSend("fila.entrega", vendaJson);
    }
    
    public void notificarEmail(String vendaJson) {
        rabbitTemplate.convertAndSend("fila.email", vendaJson);
    }
}