package com.example.grademp.networking

import com.example.grademp.room.DatabaseMemberOfFinnishParliament
import com.squareup.moshi.Json

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * Here are the network model of a finnish mp and the converter from it to database - fitting model
 */
//The network version of finnish mp's
data class NetworkMemberOfFinnishParliament(
    @Json(name = "personNumber")
    val personNumber: Int,
    @Json (name = "seatNumber")
    val seatNumber: Int,
    @Json (name = "last")
    val last: String,
    @Json (name = "first")
    val first: String,
    @Json (name = "party")
    val party: String,
    @Json (name = "minister")
    val minister: Boolean,
    @Json (name = "picture")
    val picture: String,
    @Json (name = "twitter")
    val twitter: String,
    @Json (name = "bornYear")
    val bornYear: Int,
    @Json (name = "constituency")
    val constituency: String)


//Function which maps network MP's to a list of database MP's
fun toDatabaseMP(newNetworkMPList: List<NetworkMemberOfFinnishParliament>, oldMPList :List<DatabaseMemberOfFinnishParliament>):
        List<DatabaseMemberOfFinnishParliament> {

    //On the first time the app is launched we can give neutral status to all MP's
    if (oldMPList.isEmpty()) {
        return newNetworkMPList.map {
            DatabaseMemberOfFinnishParliament(
                personNum = it.personNumber,
                first = it.first,
                last = it.last,
                bornYear = it.bornYear,
                constituency = it.constituency,
                seatNumber = it.seatNumber,
                party = it.party,
                minister = it.minister,
                picture = it.picture,
                status = 0,
                twitterLink = it.twitter
            )
        }
    } else {
        //If this is a regular update, we need to preserve the status of each MP

        //Making sure the lists are ordered similarly
        val sortedNewMPList = newNetworkMPList.sortedBy { it.personNumber }
        val sortedOldMPList = oldMPList.sortedBy { it.personNum }

        val newList = mutableListOf<DatabaseMemberOfFinnishParliament>()
        for ((i, oldMP) in sortedOldMPList.withIndex()) {
            val newMP = sortedNewMPList[i]
            newList.add(
                DatabaseMemberOfFinnishParliament(
                    personNum = newMP.personNumber,
                    first = newMP.first,
                    last = newMP.last,
                    bornYear = newMP.bornYear,
                    constituency = newMP.constituency,
                    seatNumber = newMP.seatNumber,
                    party = newMP.party,
                    minister = newMP.minister,
                    picture = newMP.picture,
                    //Here we preserve the old status
                    status = oldMP.status,
                    twitterLink = newMP.twitter)
            )
        }
        return newList
    }
}