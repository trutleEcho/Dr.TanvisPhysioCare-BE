package com.shared.utils

inline fun <T> safeTenantAction(
    orgId: String?,
    onMissing: () -> Nothing = { throw IllegalArgumentException("Missing orgId") },
    block: (orgId: String) -> T
): T = runCatching {
    if (orgId.isNullOrBlank()) onMissing()
    block(orgId)
}.getOrElse {
    throw IllegalStateException("Tenant action failed: ${it.message}", it)
}
