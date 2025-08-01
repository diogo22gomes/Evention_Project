package com.example.evention.mock

import com.example.evention.model.*
import java.util.*

object MockUserData {

    private val userTypes = listOf(
        UserType(
            userTypeID = "1",
            type = "Administrador"
        ),
        UserType(
            userTypeID = "2",
            type = "Utilizador"
        ),
        UserType(
            userTypeID = "3",
            type = "Anunciante"
        )
    )

    private val addresses = listOf(
        Address(
            addressID = "1",
            road = "Rua da Liberdade",
            roadNumber = 101,
            postCode = "4750-123",
            NIF = "123456789",
            localtown_id = "Barcelos"
        ),
        Address(
            addressID = "2",
            road = "Avenida dos Aliados",
            roadNumber = 202,
            postCode = "4000-065",
            NIF = null,
            localtown_id = "Porto"
        ),
        Address(
            addressID = "3",
            road = "Rua Direita",
            roadNumber = 303,
            postCode = "4700-303",
            NIF = "987654321",
            localtown_id = "Braga"
        ),
        Address(
            addressID = "4",
            road = "Praça da República",
            roadNumber = 404,
            postCode = "4900-404",
            NIF = null,
            localtown_id = "Viana do Castelo"
        )
    )

    val users = listOf(
        User(
            userID = "u1",
            username = "joaosilva",
            phone = 912345678,
            email = "joao.silva@email.com",
            password = null,
            status = true,
            createdAt = Date(),
            loginType = "Google",
            userType = userTypes[1],
            usertype_id = "2",
            address = addresses[0],
            profilePicture = "user1"
        ),
        User(
            userID = "u2",
            username = "mariaoliveira",
            phone = 913987654,
            email = "maria.oliveira@email.com",
            password = "senhaSegura123",
            status = true,
            createdAt = Date(),
            loginType = "Email",
            userType = userTypes[0],
            usertype_id = "1",
            address = addresses[1],
            profilePicture = "user2"
        ),
        User(
            userID = "u3",
            username = "carlossousa",
            phone = 914567890,
            email = "carlos.sousa@email.com",
            password = null,
            status = true,
            createdAt = Date(),
            loginType = "Facebook",
            userType = userTypes[2],
            usertype_id = "3",
            address = addresses[2],
            profilePicture = "user3"
        ),
        User(
            userID = "u4",
            username = "anadias",
            phone = 915678901,
            email = "ana.dias@email.com",
            password = "anaPass2024",
            status = true,
            createdAt = Date(),
            loginType = "Email",
            userType = userTypes[1],
            usertype_id = "2",
            address = addresses[3],
            profilePicture = "user4"
        ),
        User(
            userID = "u5",
            username = "pedromartins",
            phone = 916789012,
            email = "pedro.martins@email.com",
            password = null,
            status = true,
            createdAt = Date(),
            loginType = "Google",
            userType = userTypes[2],
            usertype_id = "3",
            address = addresses[0],
            profilePicture = "user5"
        ),
        User(
            userID = "u6",
            username = "sofiasantos",
            phone = 917890123,
            email = "sofia.santos@email.com",
            password = "sofialinda123",
            status = false,
            createdAt = Date(),
            loginType = "Email",
            userType = userTypes[0],
            usertype_id = "1",
            address = addresses[1],
            profilePicture = "user6"
        ),
        User(
            userID = "u7",
            username = "ricardocosta",
            phone = 918901234,
            email = "ricardo.costa@email.com",
            password = null,
            status = true,
            createdAt = Date(),
            loginType = "Google",
            userType = userTypes[2],
            usertype_id = "3",
            address = addresses[2],
            profilePicture = "user7"
        )
    )
}
