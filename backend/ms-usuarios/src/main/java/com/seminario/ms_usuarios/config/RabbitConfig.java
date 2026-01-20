package com.seminario.ms_usuarios.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    // ========== CANAL 1: MS-USUARIOS → MS-CATALOGO (Eventos de Registro) ==========
    public static final String EXCHANGE_FROM_USUARIOS = "exchange.usuarios.catalogo.v1"; 
    public static final String QUEUE_FROM_USUARIOS = "cola.usuarios.catalogo.v1";
    public static final String ROUTING_KEY_REGISTRAR_USUARIOS = "evento.vendedor.registrado.v1";

    // ========== CANAL 2: MS-CATALOGO → MS-USUARIOS (Respuestas/Actualizaciones) ==========
    public static final String EXCHANGE_TO_USUARIOS = "exchange.catalogo.usuarios.v1";
    public static final String QUEUE_TO_USUARIOS = "cola.catalogo.usuarios.v1";
    public static final String ROUTING_KEY_ACTUALIZAR_USUARIOS = "evento.vendedor.actualizado.v1";

    // ========== CANAL 1: DESDE USUARIOS ==========
    @Bean
    public TopicExchange exchangeFromUsuarios() {
        return new TopicExchange(EXCHANGE_FROM_USUARIOS, true, false);
    }

    @Bean
    public Queue queueFromUsuarios() {
        return new Queue(QUEUE_FROM_USUARIOS, true);
    }

    @Bean
    public Binding bindingFromUsuarios(Queue queueFromUsuarios, TopicExchange exchangeFromUsuarios) {
        return BindingBuilder.bind(queueFromUsuarios)
                            .to(exchangeFromUsuarios)
                            .with(ROUTING_KEY_REGISTRAR_USUARIOS);
    }

    // ========== CANAL 2: HACIA USUARIOS ==========
    @Bean
    public TopicExchange exchangeToUsuarios() {
        return new TopicExchange(EXCHANGE_TO_USUARIOS, true, false);
    }

    @Bean
    public Queue queueToUsuarios() {
        return new Queue(QUEUE_TO_USUARIOS, true);
    }

    @Bean
    public Binding bindingToUsuarios(Queue queueToUsuarios, TopicExchange exchangeToUsuarios) {
        return BindingBuilder.bind(queueToUsuarios)
                            .to(exchangeToUsuarios)
                            .with(ROUTING_KEY_ACTUALIZAR_USUARIOS);
    }

    // ========== MESSAGE CONVERTER ==========
    @Bean
    public MessageConverter jsonConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        
        typeMapper.setTrustedPackages("*");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    // ========== RABBIT TEMPLATE ==========
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}