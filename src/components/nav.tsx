'use client';

import React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  SidebarHeader,
  SidebarContent,
  SidebarMenu,
  SidebarMenuItem,
  SidebarMenuButton,
  SidebarFooter,
} from '@/components/ui/sidebar';
import { LayoutDashboard, Package, Warehouse, Users, Settings, LogOut, PackageSearch } from 'lucide-react';

const navItems = [
  { href: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { href: '/orders', label: 'Orders', icon: Package },
  { href: '/optimizer', label: 'Optimizer', icon: PackageSearch },
  { href: '/users', label: 'Users', icon: Users },
];

export default function Nav() {
  const pathname = usePathname();

  return (
    <>
      <SidebarHeader>
        <div className="flex items-center gap-2">
            <Warehouse className="w-8 h-8 text-primary" />
            <h1 className="text-2xl font-bold">PharmaFlow</h1>
        </div>
      </SidebarHeader>
      <SidebarContent className="p-2">
        <SidebarMenu>
          {navItems.map((item) => (
            <SidebarMenuItem key={item.href}>
              <Link href={item.href} passHref legacyBehavior>
                <SidebarMenuButton
                  asChild
                  isActive={pathname === item.href || (item.href !== '/dashboard' && pathname.startsWith(item.href))}
                  tooltip={item.label}
                >
                  <Link href={item.href}>
                    <item.icon />
                    <span>{item.label}</span>
                  </Link>
                </SidebarMenuButton>
              </Link>
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarContent>
      <SidebarFooter className="p-2">
        <SidebarMenu>
            <SidebarMenuItem>
                <SidebarMenuButton tooltip="Settings">
                    <Settings />
                    <span>Settings</span>
                </SidebarMenuButton>
            </SidebarMenuItem>
            <SidebarMenuItem>
                <SidebarMenuButton tooltip="Logout">
                    <LogOut />
                    <span>Logout</span>
                </SidebarMenuButton>
            </SidebarMenuItem>
        </SidebarMenu>
      </SidebarFooter>
    </>
  );
}
