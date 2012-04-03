package ajedrez.server;

import server.ServerSessionHandler;

import ajedrez.common.messages.AjedrezProtocolDecoder;

import com.google.common.collect.Lists;

public class AjedrezSessionHandler extends ServerSessionHandler {

    public AjedrezSessionHandler() {
        super(new AjedrezProtocolDecoder());

        salones = Lists.newArrayList();
        salones.add(new AjedrezSaloon(0, this));
        salones.add(new AjedrezSaloon(1, this));
        salones.add(new AjedrezSaloon(2, this));
    }

    @Override
    protected int getCodigoJuego() {
        // ajedrez = 4 para la base
        return 4;
    }
}
