package crow.teomant.modular.handshakes.user.domain.exchange.service;

import crow.teomant.modular.handshakes.common.exchange.PathResponseExchange;

public interface MqListener {

    void processPath(PathResponseExchange path);
}
