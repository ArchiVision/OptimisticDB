# OptimisticDB
Overview
This project implements a versioned database that supports Optimistic Locking and Multi-Version Concurrency Control (MVCC), 
enabling efficient and concurrent access to data with versioning. It uses optimistic locking to handle concurrency, providing a mechanism that avoids unnecessary locking of data and instead checks for conflicts only when attempting to modify the data.

# Abstraction Layers
![image](https://github.com/user-attachments/assets/876ae587-514a-4874-bc1d-45c3060501c9)

# Optimistic Locking
How It Works:
1. Versioning: Each piece of data has a version number. When data is updated, the version number is incremented.
2. Put Method: When updating data, we check if the version of the data matches the version when it was read. If not, a conflict is detected, and the update is rejected.
3. Get Method: The version number is included when data is retrieved. This version is used to check for conflicts during updates.
Handling Conflicts: If the version has changed since it was read, an OptimisticLockingException is thrown to notify the user of the conflict.
4. Handling Conflicts: If the version has changed since it was read, an OptimisticLockingException is thrown to notify the user of the conflict.

# Why It's Called "Optimistic":
The term "optimistic" comes from the assumption that conflicts will not occur frequently. Instead of locking data upfront and 
assuming that conflicts will happen, the system optimistically proceeds with the operation, only checking for conflicts at the time of 
the update. This approach maximizes concurrency and improves performance in scenarios where conflicts are rare.