function fn() {
    let config = {
        urlBase: 'http://localhost:8081/clientes'
    };
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 15000);
    return config;
}