package br.com.animal.api.service.event;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.animal.api.domain.EventLog;
import br.com.animal.api.repository.EventLogRepository;

@Aspect
@Component
public class EventLogService {

	@Autowired
	private EventLogRepository eventLogRepository;
    
	@AfterReturning(value = "@annotation(EventLogAfterReturning)", returning = "returnValue")
	public void create(JoinPoint joinPoint, Object returnValue) {
		
		EventLog eventLog = new EventLog();
		eventLog.setDescription(getValue(joinPoint));
		eventLog.setMethod(joinPoint.getSignature().getName());
		eventLog.setParameters(getParameters(joinPoint));	
		eventLog.setReturnResult(getReturn(returnValue));
		eventLog.setCreationDateTime(LocalDateTime.now());
	
		eventLogRepository.save(eventLog);
	}
	
	@AfterThrowing(value = "@annotation(EventLogAfterReturning)",  throwing  = "exception")
	public void createThrowing(JoinPoint joinPoint, Exception exception) {
		
		EventLog eventLog = new EventLog();
		eventLog.setDescription(getValue(joinPoint));
		eventLog.setMethod(joinPoint.getSignature().getName());
		eventLog.setParameters(getParameters(joinPoint));	
		eventLog.setCreationDateTime(LocalDateTime.now());
		
		//error
		eventLog.setExceptionMessage(exception.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		eventLog.setStackTrace(sw.toString());
		
		eventLogRepository.save(eventLog);
	}
	
	private List<String> getParameters(JoinPoint joinPoint) {
		
		List<String> parameters = new ArrayList<>();
		
		if(joinPoint.getArgs().length == 0) {
			
			parameters.add("void");
			
		}else {
			
			for (Object obj : joinPoint.getArgs()) {
				
				parameters.add(obj.toString());
			}
		}
		
		return parameters;
	}
	 
	private String getValue(JoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Annotation[] declaringClass = method.getAnnotations();

		for (Annotation annotation : declaringClass) {

			if (annotation instanceof Annotation) {

				EventLogAfterReturning e = method.getAnnotation(EventLogAfterReturning.class);
				return e.value();
			}
		}

		return "Anotação não encontrada";
	}
	
	private String getReturn(Object returnValue) {
		
		if(returnValue instanceof Iterable) {
			
			@SuppressWarnings("unchecked")
			Iterable<Object> list = (Iterable<Object>) returnValue;
			
			List<String> returnResult = new ArrayList<>();
			
			long size = StreamSupport.stream(list.spliterator(), true).count();
			
			if(size == 1) {
				
				returnResult.add(list.toString());
				
			}else {
				
				returnResult.add(list.toString());
				
				for (Object obj : list) {
					
					returnResult.add(obj.toString());
				}
			}
			
			return String.join(", ", returnResult);
		}
		
		return returnValue.toString();
	}
}