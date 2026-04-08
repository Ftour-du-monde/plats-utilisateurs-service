package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

final class RestTestSupport {

    private RestTestSupport() {
    }

    static UriInfo uriInfo(String requestUri) {
        URI uri = URI.create(requestUri);
        return (UriInfo) Proxy.newProxyInstance(
                UriInfo.class.getClassLoader(),
                new Class[]{UriInfo.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    switch (name) {
                        case "getRequestUri":
                            return uri;
                        case "getAbsolutePath":
                            return uri;
                        case "getRequestUriBuilder":
                        case "getAbsolutePathBuilder":
                            return UriBuilder.fromUri(uri);
                        case "getPath":
                            return uri.getPath();
                        case "getPathParameters":
                        case "getQueryParameters":
                            return new MultivaluedHashMap<>();
                        case "getMatchedResources":
                        case "getMatchedURIs":
                            return List.of();
                        default:
                            return defaultValue(method.getReturnType());
                    }
                }
        );
    }

    static ContainerRequestContext requestContext(String method, AtomicReference<Response> abortedRef) {
        return (ContainerRequestContext) Proxy.newProxyInstance(
                ContainerRequestContext.class.getClassLoader(),
                new Class[]{ContainerRequestContext.class},
                (proxy, calledMethod, args) -> {
                    String name = calledMethod.getName();
                    switch (name) {
                        case "getMethod":
                            return method;
                        case "abortWith":
                            abortedRef.set((Response) args[0]);
                            return null;
                        default:
                            return defaultValue(calledMethod.getReturnType());
                    }
                }
        );
    }

    static ContainerResponseContext responseContext(MultivaluedMap<String, Object> headers) {
        return (ContainerResponseContext) Proxy.newProxyInstance(
                ContainerResponseContext.class.getClassLoader(),
                new Class[]{ContainerResponseContext.class},
                (proxy, method, args) -> {
                    if ("getHeaders".equals(method.getName())) {
                        return headers;
                    }
                    return defaultValue(method.getReturnType());
                }
        );
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(returnType)) {
            return false;
        }
        if (byte.class.equals(returnType)) {
            return (byte) 0;
        }
        if (short.class.equals(returnType)) {
            return (short) 0;
        }
        if (int.class.equals(returnType)) {
            return 0;
        }
        if (long.class.equals(returnType)) {
            return 0L;
        }
        if (float.class.equals(returnType)) {
            return 0f;
        }
        if (double.class.equals(returnType)) {
            return 0d;
        }
        if (char.class.equals(returnType)) {
            return '\0';
        }
        return null;
    }
}
