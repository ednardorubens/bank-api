package br.com.contaazul.bankapi.model.entity.converter;

import br.com.contaazul.bankapi.model.entity.BoletoStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 *
 * @author Ednardo Rubens
 */
public class BoletoStatusDeserializer  extends JsonDeserializer<BoletoStatus> {

    @Override
    public BoletoStatus deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return BoletoStatus.valueOf(node.asText());
    }

}