import axios from "axios";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

const root_path = "http://localhost:8080/server/";

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


//=========================================
//SPECIAL CASE!!!!!!

export class SocketProvider{
    constructor() {
        this.socket = null;
        this.ws = null;
        this.memberName = null
    }

    connect = (memberName) => {
        this.memberName = memberName;
        this.socket = new SockJS('http://localhost:8080/ws/api');

        this.socket.addEventListener('open', () => {
            console.log('connected')
        })

        this.socket.addEventListener('message', (event) => {
            console.log(event)
        })
        this.ws = Stomp.over(this.socket)
        // this.socket.onclose(() => {this.ws.disconnect()})
        this.ws.connect({}, () => {
            this.ws.subscribe(`/user/queue/specific/${this.memberName}`, message => {
                alert("You have Notification: \n" + message.body);
            });
        }, function (error) {
            console.log("STOMP error " + error);
        });
    }

    disconnect = ()=>{
        this.socket.close();
    }

    // adminConnect = () =>{}

    adminConnect = () => {
        this.ws.subscribe(`/user/queue/admins/${this.memberName}`, message => {
            alert("You have Notification: \n" + message.body);
        });
    }
}


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
  street,
  zip
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
            zip:zip
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const shopPurchaseHistory = "getShopPurchaseHistory";
export const getShopPurchaseHistory = (visitor_id, shop_id) => {
    console.log(visitor_id)
    console.log(shop_id)
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

const myShops = "getMyShops";
export const getMyShops = (visitor_id) => {
    return axios
        .get(root_path + myShops, {
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
          visitorId: visitor_id,
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
          visitorId: visitor_id,
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

const addProductAmount = "addProductAmount";
export const postAddProductAmount = (visitor_id, shop_id, product_id, quan) => {
  return axios
    .post(
      root_path + addProductAmount,
      {},
      {
        params: {
          visitorId: visitor_id,
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
          visitorId: visitor_id,
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
          visitorId: visitor_id,
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
          newDescription: desc,
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
export const getShopRoleInfo = (visitor_id, shop_id) => {
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
  percent,
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
          percentage: percent,
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
  percent,
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
          percentage: percent,
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
  percent,
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
          percentage: percent,
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

const allPurchasePolicies = "getPurchasePolicies"
export const getPurchasePolicies = (visitor_id, shop_id) =>{
    return axios.get(
        root_path + allPurchasePolicies, {
            params: {
                visitorId: visitor_id,
                shopId: shop_id,
            }
        }
    ).then((response) => {
        return response.data
    })
}

const allDiscountPolicies = "getDiscountPolicies"
export const getDiscountPolicies = (visitor_id, shop_id) =>{
    return axios.get(
        root_path + allDiscountPolicies, {
            params: {
                visitorId: visitor_id,
                shopId: shop_id,
            }
        }
    ).then((response) => {
        return response.data
    })
}

const isAdmin = "getIsAdmin"
export const getIsAdmin = (visitor_id) =>{
    return axios.get(
        root_path + isAdmin, {
            params: {
                visitorId: visitor_id,
            }
        }
    ).then((response) => {
        return response.data
    })
}

const shoppingCart = "getVisitorCart"
export const getShoppingCart = (visitor_id) =>{
    return axios.get(
        root_path + shoppingCart, {
            params: {
                visitorId: visitor_id,
            }
        }
    ).then((response) => {
        return response.data
    })
}

const myBids = "getMemberBids"
export const getMyBids = (visitor_id) =>{
    return axios.get(
        root_path + myBids, {
            params: {
                visitorId: visitor_id,
            }
        }
    ).then((response) => {
        return response.data
    })
}

const shopBids = "getShopBids"
export const getShopBids = (visitor_id, shop_id) =>{
    return axios.get(
        root_path + shopBids, {
            params: {
                visitorId: visitor_id,
                shopId: shop_id,
            }
        }
    ).then((response) => {
        return response.data
    })
}

const bidProduct = "postBidProduct";
export const postBidProduct = (visitor_id, shop_id, product_id, amount, price) => {
    return axios
        .post(
            root_path + bidProduct,
            {},
            {
                params: {
                    visitorId: visitor_id,
                    shopId: shop_id,
                    productId: product_id,
                    quantity: amount,
                    price: price
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const approveBid = "approveBid";
export const postApproveBid = (visitor_id, shop_id, bid_id) => {
    return axios
        .post(
            root_path + approveBid,
            {},
            {
                params: {
                    visitorId: visitor_id,
                    shopId: shop_id,
                    bidId: bid_id
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const incBid = "incrementBid";
export const postIncrementBid = (visitor_id, shop_id, bid_id, price) => {
    return axios
        .post(
            root_path + incBid,
            {},
            {
                params: {
                    visitorId: visitor_id,
                    shopId: shop_id,
                    bidId: bid_id,
                    price: price
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const purchaseBid = "purchaseBid";
export const postPurchaseBid = (
    visitor_id,
    bid_id,
    credit_card,
    date,
    cvs,
    country,
    city,
    street,
    zip
) => {
    return axios
        .post(
            root_path + purchaseBid,
            {},
            {
                params: {
                    visitorId: visitor_id,
                    bidId: bid_id,
                    creditCardNumber: credit_card,
                    date: date,
                    cvs: cvs,
                    country: country,
                    city: city,
                    street: street,
                    zip:zip
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};
const initAssignment = "initAssignShopOwner";
export const postInitAssignmentShopOwner = (visitor_id, member_name, shop_id) => {
    return axios
        .post(
            root_path + initAssignment,
            {},
            {
                params: {
                    visitorId: visitor_id,
                    userNameToAssign: member_name,
                    shopId: shop_id,
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const approveAssignShopOwner = "approveAssignShopOwner";
export const postApproveAssignShopOwner = (visitor_id, member_name, shop_id) => {
    return axios
        .post(
            root_path + approveAssignShopOwner,
            {},
            {
                params: {
                    visitorId: visitor_id,
                    userNameToAssign: member_name,
                    shopId: shop_id,
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const shopAssignments = "getAllShopAssignments"
export const getShopAssignments = (visitor_id, shop_id) => {
    return axios
        .get(
            root_path + shopAssignments,
            {
                params: {
                    visitorId: visitor_id,
                    shopId: shop_id
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};

// ******************* ADMIN

const allMembers = "getAllMembers";
export const getAllMembers = (visitor_id) => {
    return axios
        .get(
            root_path + allMembers,
            {
                params: { visitorId: visitor_id },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const allShopOwners = "getAllShopOwners"
export const getAllShopOwners = (visitor_id) => {
    return axios
        .get(
            root_path + allShopOwners,
            {
                params: { visitorId: visitor_id },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const allVisitors = "getAllVisitors"
export const getAllVisitors = (visitor_id) => {
    return axios
        .get(
            root_path + allVisitors,
            {
                params: { visitorId: visitor_id },
            }
        )
        .then((response) => {
            return response.data;
        });
};

const dailyData = "getDailyData"
export const getDailyData = (visitor_id, start_date, end_date) => {
    return axios
        .get(
            root_path + dailyData,
            {
                params: {
                visitorId: visitor_id,
                startDate: start_date,
                endDate: end_date
                },
            }
        )
        .then((response) => {
            return response.data;
        });
};
