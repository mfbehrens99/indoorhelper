// License: AGPL. For details, see LICENSE file.
package io.controller;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;

import java.util.ArrayList;

/**
 * Listener handles import actions.
 *
 * @author rebsc
 */
public interface ImportEventListener {

    /**
     * Will be called when import action started
     *
     * @param filepath Path to BIM file
     */
    void onBIMImport(String filepath);

    /**
     * Will be called after finishing parsing
     *
     * @param ways  to render
     * @param nodes to render
     */
    void onDataParsed(ArrayList<Way> ways, ArrayList<Node> nodes);
}