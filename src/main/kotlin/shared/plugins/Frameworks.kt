package com.shared.plugins

import com.modules.doctor.models.entities.Employee
import com.modules.core.models.entities.Organization
import com.shared.Repository
import com.modules.doctor.repository.employee.EmployeeRepository
import com.modules.core.repository.organization.OrganizationRepository
import com.modules.doctor.usecases.EmployeeUseCase
import com.modules.core.useCases.OrganizationUseCase
import com.modules.queue.models.entities.Host
import com.modules.queue.models.entities.Location
import com.modules.queue.models.entities.Token
import com.modules.queue.repository.HostRepository
import com.modules.queue.repository.LocationRepository
import com.modules.queue.repository.TokenRepository
import com.modules.queue.useCases.HostUseCase
import com.modules.queue.useCases.LocationUseCase
import com.modules.queue.useCases.TokenUseCase
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

            // Core Module
            single<Repository<Organization>>(qualifier = named("orgRepo")) { OrganizationRepository() }
            single<Repository<Employee>>(qualifier = named("empRepo")) { EmployeeRepository() }

            // Queue Module
            single<Repository<Host>>(qualifier = named("hostRepo")) { HostRepository() }
            single<Repository<Location>>(qualifier = named("locRepo")) { LocationRepository() }
            single<Repository<Token>>(qualifier = named("tokRepo")) { TokenRepository() }

            /** use cases */

            // Core Module
            single { OrganizationUseCase(get(), get(qualifier = named("orgRepo"))) }
            single { EmployeeUseCase(get(), get(qualifier = named("orgRepo")), get(qualifier = named("empRepo"))) }

            // Queue Module
            single { HostUseCase(get(), get(qualifier = named("orgRepo")),  get(qualifier = named("locRepo")),get(qualifier = named("hostRepo")), get(qualifier = named("tokRepo"))) }
            single { LocationUseCase(get(), get(qualifier = named("orgRepo")), get(qualifier = named("locRepo"))) }
            single { TokenUseCase(get(), get(qualifier = named("orgRepo")), get(qualifier = named("tokRepo"))) }
        })
    }
}
