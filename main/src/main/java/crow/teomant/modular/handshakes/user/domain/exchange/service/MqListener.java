package crow.teomant.modular.handshakes.user.domain.exchange.service;

import crow.teomant.modular.handshakes.user.domain.exchange.model.PathResponseExchange;

public interface MqListener {

    void processPath(PathResponseExchange path);
}
