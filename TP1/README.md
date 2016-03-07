# TP1 - SharedShopping

Le but de ce TP est de créer une application de **listes de courses partagées**.  

## Objectifs

Les fonctionnalités cochées sont les fonctionnalités présentes dans le squelette de l'application.

- [x] Créer une nouvelle liste
- [x] Charger une liste  
- [ ] Ajouter un objet dans la liste (local puis sur la base de données)  
- [ ] Supprimer un objet dans la liste (local puis sur la base de données)  
- [ ] Modifier la quantité d'un objet dans la liste (local puis sur la base de données)  
- [ ] Modifier le prix d'un objet dans la liste (local puis sur la base de données)  

## Explications

L'application Android doit se connecter à un serveur pour récupérer et mettre à jour les données.

Aucune sécurité n'est mise en place pour protéger les listes, afin de faciliter le développement de l'application.

## Serveur

### Format des messages

Tous les messages renvoyés par le serveur sont des objets JSON et suivent la syntaxe suivante :

```
{
  "success": integer,
  "data": JSONObject,
  "message": String
}
```

- **success** est le code d'état signalant la réussite ou non de la fonction
- **data** correspond aux données renvoyées par le serveur
- **message** contient le message de réussite ou non de la fonction

**success** peut prendre les valeurs suivantes : 

| Code | Description |
| --- | --- |
| 1 | Success |
| 0 | Error |
| -1 | Invalid Parameter |
| -2 | Not Found |
| -3 | Already exists |

Par exemple :

```
{
  "success": 1,
  "data": {
    "_id": "56dc53359b91e0650e543a26",
    "name": "MaListe2",
    "__v": 4,
    "items": [
      {
        "name": "MonItem2",
        "_id": "56dc536a9b91e0650e543a28",
        "price": 0,
        "quantity": 0
      },
      {
        "name": "MonItem",
        "_id": "56dc5647037ffdf30eec2e51",
        "price": 10,
        "quantity": 15
      }
    ]
  },
  "message": "ShoppingList found"
}
```

### Routes

### Notes concernant les routes

- **MongoID** est un type contenant une Id d'un objet dans une base de données MongoDB. Il s'agit d'une chaîne de caractères. (par exemple : "56dc5647037ffdf30eec2e51")

#### GET /lists/:id || GET /lists/:name

Search a ShoppingList with a specific ID or a specific name

##### URL Parameters

| Name | Description | Type |
| --- | --- | --- |
| id || name | Id || Name of the Shopping List | MongoID || String |

##### Body Parameters

None

##### Data returned

- **ShoppingList object** if found
- **error object** else

#### POST /lists

Create a new ShoppingList

##### URL Parameters

| Name | Description | Type |
| --- | --- | --- |
| id | Id of the Shopping List | MongoID |

##### Body Parameters

| Name | Needed | Description | Type |
| --- | --- | --- | --- |
| name | Yes | Name of the ShoppingList | String |

##### Data returned

- **ShoppingList object** if created
- **error object** else

#### POST /lists/:id

Add a new item to a ShoppingList

##### URL Parameters

| Name | Description | Type |
| --- | --- | --- |
| id | Id of the Shopping List | MongoID |

##### Body Parameters

| Name | Needed | Description | Type |
| --- | --- | --- | --- |
| name | Yes | Name of the item to create | String |
| quantity | No | Name || New quantity of item | Number |
| price | No | Name || New price of item | Number |

##### Data returned

- **ShoppingList object** if created
- **error object** else

#### PUT /lists/:id

Update datas of an item of ShoppingList

##### URL Parameters

| Name | Description | Type |
| --- | --- | --- |
| id | Id of the Shopping List | MongoID |

##### Body Parameters

| Name | Needed | Description | Type |
| --- | --- | --- | --- |
| name || id | Yes | Name || Id of the item to update | String || MongoID |
| quantity | No | Name || New quantity of item | Number |
| price | No | Name || New price of item | Number |

##### Data returned

- **ShoppingList object** if edited
- **error object** else

#### DELETE /lists/:id

Delete an item from a ShoppingList

##### URL Parameters

| Name | Description | Type |
| --- | --- | --- |
| id | Id of the Shopping List | MongoID |

##### Body Parameters

| Name | Needed | Description | Type |
| --- | --- | --- | --- |
| name || id | Yes | Name || Id of the item to delete | String || MongoID |

##### Data returned

- **ShoppingList object** if deleted
- **error object** else

