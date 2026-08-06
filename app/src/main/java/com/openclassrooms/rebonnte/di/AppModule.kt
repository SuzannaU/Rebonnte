package com.openclassrooms.rebonnte.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.rebonnte.data.datasource.AisleDataSource
import com.openclassrooms.rebonnte.data.datasource.AisleFirestoreDataSource
import com.openclassrooms.rebonnte.data.datasource.HistoryDataSource
import com.openclassrooms.rebonnte.data.datasource.HistoryFirestoreDataSource
import com.openclassrooms.rebonnte.data.datasource.MedicineDataSource
import com.openclassrooms.rebonnte.data.datasource.MedicineFirestoreDataSource
import com.openclassrooms.rebonnte.data.datasource.UserDataSource
import com.openclassrooms.rebonnte.data.datasource.UserFirestoreDataSource
import com.openclassrooms.rebonnte.data.repositoryImpl.AisleRepositoryFirestoreImpl
import com.openclassrooms.rebonnte.data.repositoryImpl.HistoryRepositoryFirestoreImpl
import com.openclassrooms.rebonnte.data.repositoryImpl.MedicineRepositoryFirestoreImpl
import com.openclassrooms.rebonnte.data.repositoryImpl.UserRepositoryFirestoreImpl
import com.openclassrooms.rebonnte.data.service.FirebaseAuthService
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.useCase.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.ui.addMedicine.AddMedicineViewModel
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailViewModel
import com.openclassrooms.rebonnte.ui.aisleList.AisleListViewModel
import com.openclassrooms.rebonnte.ui.medicineDetail.MedicineDetailViewModel
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }

    single<AuthService> { FirebaseAuthService(get()) }
    single<UserDataSource> { UserFirestoreDataSource(get(), get()) }
    single<AisleDataSource> { AisleFirestoreDataSource(get()) }
    single<HistoryDataSource> { HistoryFirestoreDataSource(get()) }
    single<MedicineDataSource> { MedicineFirestoreDataSource(get()) }

    single<UserRepository> { UserRepositoryFirestoreImpl(get()) }
    single<AisleRepository> { AisleRepositoryFirestoreImpl(get()) }
    single<HistoryRepository> { HistoryRepositoryFirestoreImpl(get()) }
    single<MedicineRepository> { MedicineRepositoryFirestoreImpl(get()) }

    factory<AddMedicineUseCase> { AddMedicineUseCase(get(), get()) }
    factory<UpdateMedicineUseCase> { UpdateMedicineUseCase(get(), get()) }

    viewModel { AisleDetailViewModel(get(), get()) }
    viewModel { AisleListViewModel(get()) }
    viewModel { MedicineDetailViewModel(get(), get(), get(),get(), get()) }
    viewModel { MedicineListViewModel(get()) }
    viewModel { AddMedicineViewModel(get(), get(), get()) }
}