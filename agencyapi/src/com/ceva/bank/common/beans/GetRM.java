package com.ceva.bank.common.beans;

public class GetRM {
	private String RelationshipManagerID;
	private String RelationshipManagerEmail;

	public String getRelationshipManagerID() {
		return RelationshipManagerID;
	}

	public void setRelationshipManagerID(String relationshipManagerID) {
		RelationshipManagerID = relationshipManagerID;
	}

	public String getRelationshipManagerEmail() {
		return RelationshipManagerEmail;
	}

	public void setRelationshipManagerEmail(String relationshipManagerEmail) {
		RelationshipManagerEmail = relationshipManagerEmail;
	}

	@Override
	public String toString() {
		return "GetRM [RelationshipManagerID=" + RelationshipManagerID + ", RelationshipManagerEmail="
				+ RelationshipManagerEmail + "]";
	}
}
