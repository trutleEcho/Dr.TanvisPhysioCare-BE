package com.shared.plugins

import com.modules.core.models.entities.Employee
import com.modules.core.models.entities.Organization
import com.shared.Repository
import com.modules.core.repository.employee.EmployeeRepository
import com.modules.core.repository.organization.OrganizationRepository
import com.modules.core.useCases.EmployeeUseCase
import com.modules.core.useCases.OrganizationUseCase
import com.shared.config.Config
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            /** Koin modules */

            /** MongoDB Client */
            single<CoroutineClient> { KMongo.createClient(Config.DB_URI).coroutine }

            /** Repositories */
            single<Repository<Organization>>(qualifier = named("orgRepo")) { OrganizationRepository() }
            single<Repository<Employee>>(qualifier = named("empRepo")) { EmployeeRepository() }

            /** use cases */
            single { OrganizationUseCase(get(), get(qualifier = named("orgRepo"))) }
            single { EmployeeUseCase(get(), get(qualifier = named("orgRepo")), get(qualifier = named("empRepo"))) }
        })
    }
}
