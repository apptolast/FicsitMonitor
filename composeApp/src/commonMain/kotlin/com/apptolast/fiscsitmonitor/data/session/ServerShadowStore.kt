package com.apptolast.fiscsitmonitor.data.session

data class ServerShadow(
    val frmWsPort: Int?,
    val apiToken: String?,
)

class ServerShadowStore(private val storage: SessionStorage) {

    fun get(serverId: Int): ServerShadow = ServerShadow(
        frmWsPort = storage.getShadowFrmWsPort(serverId),
        apiToken = storage.getShadowApiToken(serverId),
    )

    fun put(serverId: Int, frmWsPort: Int?, apiToken: String?) {
        storage.putShadow(serverId, frmWsPort, apiToken)
    }

    fun remove(serverId: Int) {
        storage.removeShadow(serverId)
    }
}
