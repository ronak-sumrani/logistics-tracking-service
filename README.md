## Note: Update datasource username and password according to your local PostgreSQL installation.

# Proposed Domain Model

The following entities are expected as part of the solution.

The objective is not only to implement them but also to understand why they exist and how they relate to each other.

---

## Consignment

Represents a customer shipment.

Fields:

- id
- consignmentNumber
- senderName
- senderMobile
- receiverName
- receiverMobile
- origin
- destination
- status
- assignedVehicleId
- createdAt
- updatedAt

Responsibilities:

- Acts as the primary business entity.
- Stores the current state of a shipment.
- Represents the latest view of shipment information.

Relationship:

- One Consignment may have many Shipment History records.
- One Consignment may have many Audit Logs.
- One Consignment may be assigned to one Vehicle at a time.

---

## Vehicle

Represents a transportation vehicle.

Fields:

- id
- vehicleNumber
- vehicleType
- driverName
- driverMobile
- createdAt

Responsibilities:

- Represents the transportation resource assigned to shipments.

Relationship:

- One Vehicle may transport multiple Consignments.

Design Discussion:

For simplicity, Driver information is embedded within Vehicle.

Future versions may introduce a dedicated Driver entity.

---

## ShipmentHistory

Represents the lifecycle movement of a shipment.

Fields:

- id
- consignmentId
- oldStatus
- newStatus
- remarks
- changedAt

Responsibilities:

- Maintains complete tracking history.
- Preserves historical status changes.
- Supports shipment tracking APIs.

Relationship:

- Many ShipmentHistory records belong to one Consignment.

Important:

Current shipment status should never be derived by updating history records.

The current status should remain inside Consignment while history acts as an immutable audit trail.

---

## AuditLog

Represents operational audit records.

Fields:

- id
- consignmentId
- action
- performedBy
- performedAt

Responsibilities:

- Records important business actions.
- Supports auditing and debugging.

Examples:

Shipment Created

Vehicle Assigned

Status Updated

Notification Triggered

Relationship:

- Many AuditLog records belong to one Consignment.

Important:

Audit records should be immutable after creation.

---

## NotificationEvent

Represents notifications generated from shipment events.

Fields:

- id
- consignmentId
- channel
- message
- status
- createdAt

Responsibilities:

- Tracks notification generation.
- Helps observe asynchronous processing.
- Supports retry and delivery tracking in future enhancements.

Relationship:

- Many NotificationEvent records belong to one Consignment.

Design Discussion:

Although notifications are currently simulated through logs, this entity prepares the design for future real-world integrations.

---

## ShipmentStatusChangedEvent

Kafka Event Model

Fields:

- consignmentId
- consignmentNumber
- oldStatus
- newStatus
- timestamp

Responsibilities:

- Serves as the integration contract between producer and consumers.
- Decouples shipment processing from downstream operations.

Important:

This is not a database entity.

It is an event payload.

---

# Entity Relationship Overview

Vehicle

1

|

|

N

Consignment

|

|

+------ N ShipmentHistory

```
|
|
+------ N AuditLog

|
|
+------ N NotificationEvent
```

---

# Future Extensibility

The design should allow future introduction of:

- Driver
- Challan
- Route
- Hub
- Customer
- Notification Preference
- Vehicle Assignment History

without requiring major redesign of existing modules.