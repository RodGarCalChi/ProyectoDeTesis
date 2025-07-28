import React from 'react';
import { SidebarProvider, Sidebar, SidebarInset } from '@/components/ui/sidebar';
import AppHeader from '@/components/app-header';
import Nav from '@/components/nav';

export default function AppLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <SidebarProvider>
      <Sidebar>
        <Nav />
      </Sidebar>
      <SidebarInset>
        <AppHeader />
        <main className="p-4 lg:p-6">{children}</main>
      </SidebarInset>
    </SidebarProvider>
  );
}
