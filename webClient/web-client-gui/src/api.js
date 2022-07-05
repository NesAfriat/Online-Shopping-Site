import axios from "axios";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

const root_path = "http://localhost:8080/server/";
const login_path = "http://localhost:8080/login";

const visit = "visit";
export const postVisit = () => {
  return axios.post(root_path + visit).then((response) => {
    return response.data;
  });
};

const leave = "leave";
export const postLeave = (visitor_id) => {
  return axios
    .post(root_path + leave, {}, { params: { visitorId: visitor_id } })
    .then((response) => {
      return response.data;
    });
};

const register = "register";
export const postRegister = (visitor_id, user_name, pass, email) => {
  return axios
    .post(
      root_path + register,
      {},
      {
        params: {
          visitorId: visitor_id,
          username: user_name,
          password: pass,
          email: email,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

//++++++++++++++++++++++++++++++++++++++++
// TESTS!
// export const wsRegisterTest = (visitor_id) => {
//   const wsRegister = `http://localhost:8080/ws`;
//   let socket = new SockJS(wsRegister);
//   const ws = Stomp.over(socket);
//   ws.connect(
//     {},
//     (onConnected = () => {
//       ws.subscribe(
//         `/chatroom/public`,
//         (onPublicMessageReceive = (payload) => {
//           let payloadData = JSON.parse(payload.body);
//           switch (payloadData.status) {
//             case "JOIN":
//               break;
//             case "LEAVE":
//               break;
//             case "MESSAGE":
//               //show in the ui somewhere
//               break;
//           }
//         })
//       );
//       ws.subscribe(
//         `/user/${visitor_id}/private`,
//         (onPrivateMessageReceive = (payload) => {
//           let payloadData = JSON.parse(payload.body);
//           if (payloadData.senderName === "server") {
//             //show in the ui somewhere
//           } else {
//           }
//         })
//       );
//     }),
//     onError
//   );
//   //after subscribing:
//   ws.send(
//     `app/message`,
//     {},
//     JSON.stringify({ senderName: "client", status: "JOIN" })
//   );
//   //public message
//   ws.send(
//     `/app/message`,
//     {},
//     JSON.stringify({ senderName: "client", message: "Hello" })
//   );
//   //private message
//   ws.send(
//     `/app/private-message`,
//     {},
//     JSON.stringify({
//       senderName: "client",
//       receiverName: "user2",
//       message: "Hello",
//     })
//   );
// };
//++++++++++++++++++++++++++++++++++++++++

//=========================================
//SPECIAL CASE!!!!!!
// export const postLogin = (visitor_id, user_name, pass) => {
//   const socket = new SockJS(login_path);
//   const ws = Stomp.over(socket);
//   ws.connect(
//     {},
//     () => {
//       //connect Callback
//       ws.subscribe(`user/${visitor_id}/reply`, (message) => {
//         // view in ui
//         alert("Got message " + message.body);
//       });
//     },
//     (error) => {
//       //error Callback
//       alert("Got Error " + error.body);
//     }
//   );
// };
const login = "login";
export const postLogin = (visitor_id, user_name, pass) => {
  return axios
    .post(
      root_path + login,
      {},
      {
        params: {
          visitorId: visitor_id,
          username: user_name,
          password: pass,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};
//=========================================
const logout = "logout";
export const postLogout = (visitor_id) => {
  return axios
    .post(
      root_path + logout,
      {},
      {
        params: { visitorId: visitor_id },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addProductToShoppingCart = "addProductToShoppingCart";
export const postAddProductToShoppingCart = (
  visitor_id,
  shop_id,
  product_id,
  quan
) => {
  return axios
    .post(
      root_path + addProductToShoppingCart,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productId: product_id,
          quantity: quan,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const removeProductFromShoppingCart = "removeProductFromShoppingCart";
export const postremoveProductFromShoppingCart = (
  visitor_id,
  shop_id,
  product_id
) => {
  return axios
    .post(
      root_path + removeProductFromShoppingCart,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productId: product_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const purchaseShoppingCart = "purchaseShoppingCart";
export const postPurchaseShoppingCart = (
  visitor_id,
  credit_card,
  date,
  cvs,
  country,
  city,
  street
) => {
  return axios
    .post(
      root_path + purchaseShoppingCart,
      {},
      {
        params: {
          visitorId: visitor_id,
          creditCardNumber: credit_card,
          date: date,
          cvs: cvs,
          country: country,
          city: city,
          street: street,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const shopPurchaseHistory = "getShopPurchaseHistory";
export const getShopPurchaseHistory = (visitor_id, shop_id) => {
  return axios
    .get(root_path + shopPurchaseHistory, {
      params: {
        visitorId: visitor_id,
        shopId: shop_id,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const shopInfo = "getShopInfo";
export const getShopInfo = (visitor_id, shop_id) => {
  return axios
    .get(root_path + shopInfo, {
      params: {
        visitorId: visitor_id,
        shopId: shop_id,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const searchProductByName = "searchProductByName";
export const getSearchProductByName = (visitor_id, product_name) => {
  return axios
    .get(root_path + searchProductByName, {
      params: {
        visitorId: visitor_id,
        productName: product_name,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const searchProductByCategory = "searchProductByCategory";
export const getSearchProductByCategory = (visitor_id, cat) => {
  return axios
    .get(root_path + searchProductByCategory, {
      params: {
        visitorId: visitor_id,
        category: cat,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const searchProductByKeyWord = "searchProductByKeyWord";
export const getSearchProductByKeyWord = (visitor_id, key) => {
  return axios
    .get(root_path + searchProductByKeyWord, {
      params: {
        visitorId: visitor_id,
        keyWord: key,
      },
    })
    .then((response) => {
      return response.data;
    });
};
const getAllShops = "getAllOpenShops";
export const getAllOpenShops = (visitor_id) => {
  return axios
    .get(root_path + getAllShops, {
      params: {
        visitorId: visitor_id,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const openShop = "openShop";
export const postOpenShop = (
  visitor_id,
  phone,
  card,
  shop_name,
  shop_desc,
  shop_loc
) => {
  return axios
    .post(
      root_path + openShop,
      {},
      {
        params: {
          visitorId: visitor_id,
          memberPhone: phone,
          creditCard: card,
          shopName: shop_name,
          shopDescription: shop_desc,
          shopLocation: shop_loc,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const closeShop = "closeShop";
export const postCloseShop = (visitor_id, shop_id) => {
  return axios
    .post(
      root_path + closeShop,
      {},
      {
        params: {
          visitorIdFounder: visitor_id,
          shopId: shop_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const reOpenShop = "reOpenShop";
export const postReOpenShop = (visitor_id, shop_id) => {
  return axios
    .post(
      root_path + reOpenShop,
      {},
      {
        params: {
          visitorIdFounder: visitor_id,
          shopId: shop_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const assignShopOwner = "assignShopOwner";
export const postAssignShopOwner = (visitor_id, user_name, shop_id) => {
  return axios
    .post(
      root_path + assignShopOwner,
      {},
      {
        params: {
          visitorId: visitor_id,
          usernameToAssign: user_name,
          shopId: shop_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const assignShopManager = "assignShopManager";
export const postAssignShopManager = (visitor_id, user_name, shop_id) => {
  return axios
    .post(
      root_path + assignShopManager,
      {},
      {
        params: {
          visitorId: visitor_id,
          usernameToAssign: user_name,
          shopId: shop_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addProduct = "addProduct";
export const postAddProduct = (
  visitor_id,
  shop_id,
  product_name,
  quan,
  price,
  desc,
  cat
) => {
  return axios
    .post(
      root_path + addProduct,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productName: product_name,
          quantity: quan,
          price: price,
          description: desc,
          category: cat,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const removeProduct = "removeProduct";
export const postRemoveProduct = (visitor_id, shop_id, product_id) => {
  return axios
    .post(
      root_path + removeProduct,
      {},
      {
        params: {
          visitorIdAssign: visitor_id,
          shopId: shop_id,
          productId: product_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addProductAmount = "addProductAmount";
export const postAddProductAmount = (visitor_id, shop_id, product_id, quan) => {
  return axios
    .post(
      root_path + addProductAmount,
      {},
      {
        params: {
          visitorIdAssign: visitor_id,
          shopId: shop_id,
          productId: product_id,
          amount: quan,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const reduceProductAmount = "reduceProductAmount";
export const postReduceProductAmount = (
  visitor_id,
  shop_id,
  product_id,
  quan
) => {
  return axios
    .post(
      root_path + reduceProductAmount,
      {},
      {
        params: {
          visitorIdAssign: visitor_id,
          shopId: shop_id,
          productId: product_id,
          amount: quan,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const changeProductName = "changeProductName";
export const postChangeProductName = (
  visitor_id,
  shop_id,
  product_id,
  name
) => {
  return axios
    .post(
      root_path + changeProductName,
      {},
      {
        params: {
          visitorIdAssign: visitor_id,
          shopId: shop_id,
          productId: product_id,
          newName: name,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const changeProductDescription = "changeProductDescription";
export const postChangeProductDescription = (
  visitor_id,
  shop_id,
  product_id,
  desc
) => {
  return axios
    .post(
      root_path + changeProductDescription,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productId: product_id,
          description: desc,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const updateProductPrice = "updateProductPrice";
export const postUpdateProductPrice = (
  visitor_id,
  shop_id,
  product_id,
  price
) => {
  return axios
    .post(
      root_path + updateProductPrice,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productId: product_id,
          price: price,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const shopRoleInfo = "getShopRoleInfo";
export const getGetShopRoleInfo = (visitor_id, shop_id) => {
  return axios
    .get(root_path + shopRoleInfo, {
      params: {
        visitorId: visitor_id,
        shopId: shop_id,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const permissionsOfMember = "getPermissionsOfMember";
export const getPermissionsOfMember = (visitor_id, shop_id, member_name) => {
  return axios
    .get(root_path + permissionsOfMember, {
      params: {
        visitorId: visitor_id,
        memberUserName: member_name,
        shopId: shop_id,
      },
    })
    .then((response) => {
      return response.data;
    });
};

const removePermission = "removePermission";
export const postRemovePermission = (
  visitor_id,
  shop_id,
  member_name,
  perm
) => {
  return axios
    .post(
      root_path + removePermission,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          memberUserName: member_name,
          permission: perm,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const setPermission = "setPermission";
export const postSetPermission = (visitor_id, shop_id, member_name, perm) => {
  return axios
    .post(
      root_path + setPermission,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          memberUserName: member_name,
          permission: perm,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const cancelMemberShip = "cancelMemberShip";
export const postCancelMemberShip = (visitor_id, member_name) => {
  return axios
    .post(
      root_path + cancelMemberShip,
      {},
      {
        params: {
          visitorId: visitor_id,
          memberUserName: member_name,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addProductDiscount = "addProductDiscount";
export const postAddProductDiscount = (
  visitor_id,
  shop_id,
  precent,
  expireY,
  expireM,
  expireD,
  product_id
) => {
  return axios
    .post(
      root_path + addProductDiscount,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          precentage: precent,
          expireYear: expireY,
          expireMonth: expireM,
          expireDay: expireD,
          productId: product_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addCategoryDiscount = "addCategoryDiscount";
export const postAddCategoryDiscount = (
  visitor_id,
  shop_id,
  precent,
  expireY,
  expireM,
  expireD,
  cat
) => {
  return axios
    .post(
      root_path + addCategoryDiscount,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          precentage: precent,
          expireYear: expireY,
          expireMonth: expireM,
          expireDay: expireD,
          category: cat,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addTotalShopDiscount = "addTotalShopDiscount";
export const postAddTotalShopDiscount = (
  visitor_id,
  shop_id,
  precent,
  expireY,
  expireM,
  expireD
) => {
  return axios
    .post(
      root_path + addTotalShopDiscount,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          precentage: precent,
          expireYear: expireY,
          expireMonth: expireM,
          expireDay: expireD,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addXorDiscount = "addXorDiscount";
export const postAddXorDiscount = (
  visitor_id,
  shop_id,
  discount_aid,
  discount_bid,
  expireY,
  expireM,
  expireD
) => {
  return axios
    .post(
      root_path + addXorDiscount,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          discountAid: discount_aid,
          discountBid: discount_bid,
          expireYear: expireY,
          expireMonth: expireM,
          expireDay: expireD,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addPQCondition = "addPQCondition";
export const postAddPQCondition = (
  visitor_id,
  shop_id,
  type,
  discount_id,
  product_id,
  min
) => {
  return axios
    .post(
      root_path + addPQCondition,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          type: type,
          discountId: discount_id,
          productId: product_id,
          minProductQuantity: min,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addTBPCondition = "addTBPCondition";
export const postAddTBPCondition = (
  visitor_id,
  shop_id,
  type,
  discount_id,
  min
) => {
  return axios
    .post(
      root_path + addTBPCondition,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          type: type,
          discountId: discount_id,
          minBasketPrice: min,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addAtMostFromProductPolicy = "addAtMostFromProductPolicy";
export const postAddAtMostFromProductPolicy = (
  visitor_id,
  shop_id,
  product_id,
  max
) => {
  return axios
    .post(
      root_path + addAtMostFromProductPolicy,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productId: product_id,
          maxQuantity: max,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const addAtLeastFromProductPolicy = "addAtLeastFromProductPolicy";
export const postAddAtLeastFromProductPolicy = (
  visitor_id,
  shop_id,
  product_id,
  min
) => {
  return axios
    .post(
      root_path + addAtLeastFromProductPolicy,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          productId: product_id,
          minQuantity: min,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const composePurchasePolicies = "composePurchasePolicies";
export const postComposePurchasePolicies = (
  visitor_id,
  shop_id,
  type,
  policy_Id1,
  policy_Id2
) => {
  return axios
    .post(
      root_path + composePurchasePolicies,
      {},
      {
        params: {
          visitorId: visitor_id,
          shopId: shop_id,
          type: type,
          policyId1: policy_Id1,
          policyId2: policy_Id2,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const getAllMembers = "getAllMembers";
export const postGetAllMembers = (visitor_id) => {
  return axios
    .post(
      root_path + getAllMembers,
      {},
      {
        params: { visitorId: visitor_id },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const removeShopOwner = "removeShopOwner";
export const postRemoveShopOwner = (visitor_id, member_name, shop_id) => {
  return axios
    .post(
      root_path + removeShopOwner,
      {},
      {
        params: {
          visitorId: visitor_id,
          memberUserName: member_name,
          shopId: shop_id,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};
