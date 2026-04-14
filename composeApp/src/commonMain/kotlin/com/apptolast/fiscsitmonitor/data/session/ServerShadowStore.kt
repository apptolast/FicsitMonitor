package com.apptolast.fiscsitmonitor.data.session

class ServerShadowStore(private val storage: SessionStorage) {

    fun getFrmWsPort(serverId: Int): Int? = storage.getShadowFrmWsPort(serverId)

    fun put(serverId: Int, frmWsPort: Int?) {
        storage.putShadow(serverId, frmWsPort)
    }

    fun remove(serverId: Int) {
        storage.removeShadow(serverId)
    }
}
