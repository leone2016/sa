//package edu.miu.cs.cs425.productservice;
//
//import feign.FeignException;
//import feign.RetryableException;
//import org.springframework.context.annotation.Bean;
//
//public class ErrorDecoder {
//    @Bean
//    public ErrorDecoder errorDecoder() {
//        return (methodKey, response) -> {
//            if (response.status() == 503) {
//                return new RetryableException(
//                        response.status(), "Service Unavailable", response.request().httpMethod(),
//                        null, response.request()
//                );
//            }
//            return FeignException.errorStatus(methodKey, response);
//        };
//    }
//}

