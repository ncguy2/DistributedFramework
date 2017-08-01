package net.shared.distributed.ui.form;

import net.shared.distributed.CoreStart;
import net.shared.distributed.capabilities.Capabilities;
import net.shared.distributed.capabilities.CapabilityPacket;
import net.shared.distributed.event.EventBus;
import net.shared.distributed.event.EventHandledEvent;
import net.shared.distributed.event.NodeConnectedEvent;
import net.shared.distributed.event.NodeDisconnectedEvent;
import net.shared.distributed.event.host.CapabilityResponseEvent;
import net.shared.distributed.event.node.NameResponseEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

public class CoreHost implements NodeConnectedEvent.NodeConnectedListener, NodeDisconnectedEvent.NodeDisconnectedListener,
        EventHandledEvent.EventHandledListener<CapabilityResponseEvent>, NameResponseEvent.NameResponseListener {

    private JPanel rootPanel;
    private JList<ListEntry> nodeList;
    private JList<String> capabilityList;

    private List<ListEntry> nameMap;
    int selectedNode = -1;

    public CoreHost() {
        nameMap = new ArrayList<>();
        EventBus.instance().register(this);

        nodeList.addListSelectionListener(e -> SelectNode(nodeList.getSelectedValue().id));
    }

    public JPanel GetRootPanel() {
        return rootPanel;
    }

    public void SelectNode(int nodeId) {
        selectedNode = nodeId;
        Vector<String> capabilities = new Vector<>(CoreStart.distributor.GetNodeCapabilityStream(nodeId)
                .filter(Capabilities.instance()::IsExternal)
                .collect(Collectors.toList()));
        capabilityList.setListData(capabilities);
    }

    public void InvalidateNodeList() {
        nodeList.setListData(new Vector<>(nameMap));
    }

    @Override
    public void onNodeConnected(NodeConnectedEvent event) {
        nameMap.add(new ListEntry(event.id, "Node " + event.id));
        InvalidateNodeList();
    }

    @Override
    public void onNodeDisconnected(NodeDisconnectedEvent event) {
        nameMap.remove(event.id);
        InvalidateNodeList();
    }

    public Optional<ListEntry> FindInModel(int id) {
        for (ListEntry listEntry : nameMap) {
            if(listEntry.id == id)
                return Optional.of(listEntry);
        }

        return Optional.empty();
    }

    @Override
    public void OnEventHandled(EventHandledEvent<CapabilityResponseEvent> event) {
        CapabilityResponseEvent handledEvent = event.handledEvent;
        if(selectedNode == handledEvent.id)
            SelectNode(selectedNode);

        CapabilityPacket.NameRequest nameReq = new CapabilityPacket.NameRequest();
        CoreStart.distributor.nodeSockets.get(handledEvent.id).sendTCP(nameReq);
    }

    @Override
    public void onNameResponse(NameResponseEvent event) {
        FindInModel(event.id).ifPresent(e -> e.name = event.name);
        InvalidateNodeList();
    }

    public static class ListEntry {
        public Integer id;
        public String name;

        public ListEntry(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
