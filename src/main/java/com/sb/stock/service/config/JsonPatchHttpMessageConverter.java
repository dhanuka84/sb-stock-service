package com.sb.stock.service.config;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonReader;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JsonPatchHttpMessageConverter implements HttpMessageReader<JsonPatch> {

    /*
     * public JsonPatchHttpMessageConverter() {
     * super(MediaType.valueOf("application/json-patch+json")); }
     * 
     * @Override protected JsonPatch readInternal(Class<? extends JsonPatch> clazz,
     * HttpInputMessage inputMessage) throws HttpMessageNotReadableException { try
     * (JsonReader reader = Json.createReader(inputMessage.getBody())) { return
     * Json.createPatch(reader.readArray()); } catch (Exception e) { throw new
     * HttpMessageNotReadableException(e.getMessage(), inputMessage); } }
     * 
     * @Override protected void writeInternal(JsonPatch jsonPatch, HttpOutputMessage
     * outputMessage) throws HttpMessageNotWritableException { throw new
     * HttpMessageNotWritableException("The write Json patch is not implemented"); }
     */

    protected boolean supports(Class<?> clazz) {
	return JsonPatch.class.isAssignableFrom(clazz);
    }

    @Override
    public List<MediaType> getReadableMediaTypes() {
	// TODO Auto-generated method stub
	return Collections.singletonList(MediaType.valueOf("application/json-patch+json"));
    }

    @Override
    public boolean canRead(ResolvableType elementType, MediaType mediaType) {
	// TODO Auto-generated method stub
	return MediaType.valueOf("application/json-patch+json").includes(mediaType);
    }

    @Override
    public Flux<JsonPatch> read(ResolvableType elementType, ReactiveHttpInputMessage message,
	    Map<String, Object> hints) {
	// TODO Auto-generated method stub
	return Flux.empty();
    }

    @Override
    public Mono<JsonPatch> readMono(ResolvableType elementType, ReactiveHttpInputMessage inputMessage,
	    Map<String, Object> hints) {

	Mono<DataBuffer> dataBuffer = DataBufferUtils.join(inputMessage.getBody());
	return dataBuffer.map(buffer -> readBuffer(buffer.asInputStream()));

    }

    private JsonPatch readBuffer(InputStream in) {
	try (JsonReader reader = Json.createReader(in)) {
	    return Json.createPatch(reader.readArray());
	} catch (Exception e) {
	    throw new HttpMessageNotReadableException(e.getMessage(), e);
	}
    }

}
