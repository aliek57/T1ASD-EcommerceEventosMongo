package br.edu.utfpr.td.tsi.ecommerce.pagamento;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagamentoPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void solicitarBaixaEstoque(String vendaJson) {
        rabbitTemplate.convertAndSend("fila.estoque", vendaJson);
    }
    
    public void solicitarEmissaoNF(String vendaJson) {
        rabbitTemplate.convertAndSend("fila.fiscal", vendaJson);
    }
    
    public void notificarEmail(String emailJson) {
        rabbitTemplate.convertAndSend("fila.email", emailJson);
    }
}