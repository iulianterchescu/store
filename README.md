# Store Management Tool

This is an application that allows users to utilize a range of functionalities to aid them in maintaining correct and precise inventories of items listed for sale in the store. 
There can be two types of users of this application (User and Admin), and each of these types has different roles in store management.

## Controllers Overview

In this application, controllers serve as the entry points for HTTP requests. They manage incoming requests related to products, inventory and authentication.

### 1. ProductController 

- **Responsibilities**: Manages HTTP requests related to product data from an USER perspective
- **Base Url**: /api/v1/product 
- **Endpoints**: 
  - **GET `/{productCatalogNumber}`**:
    - **PathVariable**: productCatalogNumber 
    - **Functionality**: Fetches the product with the same productCatalogNumber.
    - **Return Type**: `ResponseEntity<ProductDto>`
    - **Description**: Calls `service.findProduct()` to retrieve the a Product and returns a DTO of the product in a response entity.
   
  - **POST `/add/{numberOfProducts}`**:
    - **RequestBody**: ProductDto
    - **PathVariable**: numberOfProducts  
    - **Functionality**: Add a product in the inventory.
    - **Return Type**: `ResponseEntity<Void>`
    - **Description**: Calls `service.addProduct()` a number of x(numberOfProducts) products of type productDto in the database.
   
  - **PUT `/changePrice/{productCatalogNumber}/{newPrice}`**:
    - **PathVariable**: productCatalogNumber
    - **PathVariable**: newPrice  
    - **Functionality**: Change the price of a product.
    - **Return Type**: `ResponseEntity<Void>`
    - **Description**: Calls `service.changePrice()` to change the price of the product with the productCatalogNumber.
   
  - **PUT `/sellOne/{productCatalogNumber}`**:
    - **PathVariable**: productCatalogNumber
    - **Functionality**: Sell a product.
    - **Return Type**: `ResponseEntity<ProductDto>`
    - **Description**: Calls `service.sellOneProduct()` to decrease the number of products for the product with the productCatalogNumber by one.
   
  - **PUT `/sellMore}`**:
    - **RequestBody**: Map<UUID, Integer>
    - **Functionality**: Sell a list of products.
    - **Return Type**: `ResponseEntity<Map<ProductDto, Integer>>`
    - **Description**: Calls `service.sellMoreProducts()` to decrease the number of products for the products associated with the keys of the map by the correspondent values of the same map.


### 2. InventoryController 
- **Responsibilities**: Manages HTTP requests related to product data but from an ADMIN perspective
- **Base Url**: /api/v1/inventory
- **Endpoints**: 
  - **DELETE `/delete/{productCatalogNumber}`**:
    - **PathVariable**: productCatalogNumber
    - **Functionality**: Remove a product from the inventory.
    - **Return Type**: `ResponseEntity<Void>`
    - **Description**: Calls `service.removeProductFromInventory()` to remove the product with the productCatalogNumber from database.
   
  - **GET `/`**:
    - **Functionality**: Remove a product from the inventory.
    - **Return Type**: `ResponseEntity<Map<UUID, Integer>>`
    - **Description**: Calls `service.getProductsInventory()` to return the products inventory as a map of productCatalogNumber and productNumber.

### 3. AuthController 
