// Base types
export interface BaseEntity {
  id: string;
  createdAt: Date;
  updatedAt: Date;
}

// User Management
export interface User extends BaseEntity {
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  isActive: boolean;
  roleId: string;
  role?: Role;
}

export interface Role extends BaseEntity {
  name: string;
  description?: string;
  permissions: Permission[];
}

export interface Permission {
  id: string;
  name: string;
  resource: string;
  action: string;
}

// Product Management
export interface Product extends BaseEntity {
  name: string;
  sku: string;
  description?: string;
  categoryId: string;
  category?: Category;
  supplierId: string;
  supplier?: Supplier;
  brandId?: string;
  brand?: Brand;
  unitPrice: number;
  minStock: number;
  maxStock: number;
  isActive: boolean;
  expirationRequired: boolean;
}

export interface Category extends BaseEntity {
  name: string;
  description?: string;
  parentId?: string;
  parent?: Category;
  children?: Category[];
}

export interface Supplier extends BaseEntity {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  isActive: boolean;
}

export interface Brand extends BaseEntity {
  name: string;
  description?: string;
}

// Inventory Management
export interface Inventory extends BaseEntity {
  productId: string;
  product?: Product;
  locationId: string;
  location?: Location;
  quantity: number;
  reservedQuantity: number;
  availableQuantity: number;
  batchNumber?: string;
  expirationDate?: Date;
  lastMovementDate: Date;
}

export interface Location extends BaseEntity {
  code: string;
  name: string;
  warehouseId: string;
  warehouse?: Warehouse;
  zone: string;
  aisle: string;
  shelf: string;
  position: string;
  isActive: boolean;
  capacity: number;
}

export interface Warehouse extends BaseEntity {
  name: string;
  address: string;
  isActive: boolean;
  managerId: string;
  manager?: User;
}

// Order Management
export interface Order extends BaseEntity {
  orderNumber: string;
  type: 'PURCHASE' | 'SALE' | 'TRANSFER';
  status: 'PENDING' | 'CONFIRMED' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  customerId?: string;
  customer?: Customer;
  supplierId?: string;
  supplier?: Supplier;
  orderDate: Date;
  expectedDate?: Date;
  deliveryDate?: Date;
  totalAmount: number;
  notes?: string;
  items: OrderItem[];
}

export interface OrderItem extends BaseEntity {
  orderId: string;
  productId: string;
  product?: Product;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  receivedQuantity?: number;
  notes?: string;
}

export interface Customer extends BaseEntity {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  taxId: string;
  isActive: boolean;
}

// Receiving & Dispatching
export interface Receiving extends BaseEntity {
  receiptNumber: string;
  orderId?: string;
  order?: Order;
  supplierId: string;
  supplier?: Supplier;
  receivedDate: Date;
  receivedBy: string;
  status: 'PENDING' | 'PARTIAL' | 'COMPLETED';
  notes?: string;
  items: ReceivingItem[];
}

export interface ReceivingItem extends BaseEntity {
  receivingId: string;
  productId: string;
  product?: Product;
  expectedQuantity: number;
  receivedQuantity: number;
  batchNumber?: string;
  expirationDate?: Date;
  locationId: string;
  location?: Location;
  notes?: string;
}

export interface Dispatch extends BaseEntity {
  dispatchNumber: string;
  orderId: string;
  order?: Order;
  dispatchDate: Date;
  dispatchedBy: string;
  status: 'PENDING' | 'PARTIAL' | 'COMPLETED';
  notes?: string;
  items: DispatchItem[];
}

export interface DispatchItem extends BaseEntity {
  dispatchId: string;
  productId: string;
  product?: Product;
  requestedQuantity: number;
  dispatchedQuantity: number;
  locationId: string;
  location?: Location;
  batchNumber?: string;
  notes?: string;
}

// Stock Movements
export interface StockMovement extends BaseEntity {
  productId: string;
  product?: Product;
  locationId: string;
  location?: Location;
  movementType: 'IN' | 'OUT' | 'TRANSFER' | 'ADJUSTMENT';
  quantity: number;
  previousQuantity: number;
  newQuantity: number;
  reason: string;
  referenceId?: string;
  referenceType?: string;
  performedBy: string;
  batchNumber?: string;
}

// Reports
export interface Report extends BaseEntity {
  name: string;
  type: 'INVENTORY' | 'SALES' | 'PURCHASES' | 'MOVEMENTS' | 'EXPIRATION';
  parameters: Record<string, any>;
  generatedBy: string;
  generatedAt: Date;
  filePath?: string;
}

// Dashboard Stats
export interface DashboardStats {
  totalProducts: number;
  lowStockProducts: number;
  expiringSoonProducts: number;
  pendingOrders: number;
  todayReceivings: number;
  todayDispatches: number;
  totalValue: number;
  topProducts: Array<{
    product: Product;
    quantity: number;
    value: number;
  }>;
}